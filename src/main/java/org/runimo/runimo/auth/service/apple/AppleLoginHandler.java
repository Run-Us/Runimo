package org.runimo.runimo.auth.service.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.EncryptUtil;
import org.runimo.runimo.auth.service.dto.AuthResult;
import org.runimo.runimo.auth.service.dto.AuthStatus;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.auth.service.kakao.AppleUserInfo;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AppleLoginHandler {

    private final AppleTokenVerifier appleTokenVerifier;
    private final JwtTokenFactory jwtTokenFactory;
    private final OAuthInfoRepository oAuthInfoRepository;
    private final SignupTokenRepository signupTokenRepository;
    private final EncryptUtil encryptUtil;

    /**
     * 애플 로그인 검증을 처리하고 사용자를 로그인 또는 회원가입으로 안내합니다.
     *
     * @param authCode iOS 앱에서 받은 oidc 인증 코드.
     * @param verifier iOS 앱에서 받은 PKCE용 Verifier.
     * @return 인증 결과를 담은 객체; 로그인 성공 시 사용자 정보와 토큰 쌍을 포함합니다. 실패시 회원가입 토큰을 반환합니다.
     */
    @Transactional
    public AuthResult validateAndLogin(final String authCode, final String verifier) {
        TokenPair appleTokens = appleTokenVerifier.getAppleTokenPairFromAuthCode(authCode,
            verifier);
        DecodedJWT decodedJWT = decodeJwtOrThrow(appleTokens.accessToken());
        AppleUserInfo userInfo = appleTokenVerifier.verifyTokenAndExtractUserInfo(decodedJWT);
        Optional<OAuthInfo> oAuthInfo = oAuthInfoRepository.findByProviderAndProviderId(
            SocialProvider.APPLE,
            userInfo.getProviderId());

        if (oAuthInfo.isEmpty()) {
            SignupToken signupToken = createTemporalSignupToken(userInfo, appleTokens);
            String clientToken = jwtTokenFactory.generateSignupTemporalToken(
                userInfo.getProviderId(),
                SocialProvider.APPLE,
                signupToken.getToken()
            );
            return AuthResult.signupNeeded(AuthStatus.SIGNUP_NEEDED, clientToken);
        }

        TokenPair tokenPair = jwtTokenFactory.generateTokenPair(oAuthInfo.get().getUser());
        return AuthResult.success(AuthStatus.LOGIN_SUCCESS, oAuthInfo.get().getUser(), tokenPair);
    }

    public SignupToken createTemporalSignupToken(AppleUserInfo appleUserInfo, TokenPair appleTokens) {
        String temporalTokenId = UUID.randomUUID().toString();
        String encryptedRefreshToken = encryptUtil.encrypt(appleTokens.refreshToken());
        SignupToken signupToken = new SignupToken(
            temporalTokenId,
            appleUserInfo.getProviderId(),
            encryptedRefreshToken,
            SocialProvider.APPLE
        );
        return signupTokenRepository.save(signupToken);
    }

    private DecodedJWT decodeJwtOrThrow(String jwt) {
        try {
            return JWT.decode(jwt);
        } catch (JWTDecodeException e) {
            throw UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID);
        }
    }
}
