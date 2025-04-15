package org.runimo.runimo.auth.service.kakao;

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
import org.runimo.runimo.auth.service.apple.KakaoUserInfo;
import org.runimo.runimo.auth.service.dtos.AuthResult;
import org.runimo.runimo.auth.service.dtos.AuthStatus;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KakaoLoginHandler {

    private final KakaoTokenVerifier kakaoTokenVerifier;
    private final OAuthInfoRepository oAuthInfoRepository;
    private final JwtTokenFactory jwtTokenFactory;
    private final SignupTokenRepository signupTokenRepository;

    /**
     * 카카오 로그인 검증을 처리하고 사용자를 로그인 또는 회원가입으로 안내합니다.
     *
     * @param rawToken iOS 앱에서 받은 카카오 oidc 토큰
     * @return 인증 결과를 담은 객체; 로그인 성공 시 사용자 정보와 토큰 쌍을 포함합니다. 실패시 회원가입 토큰을 반환합니다.
     */
    @Transactional
    public AuthResult validateAndLogin(final String rawToken) {
        DecodedJWT token = decodeJwtOrThrow(rawToken);
        KakaoUserInfo kakaoUserInfo = kakaoTokenVerifier.verifyToken(token);
        Optional<OAuthInfo> oAuthInfo = oAuthInfoRepository.findByProviderAndProviderId(
            SocialProvider.KAKAO,
            kakaoUserInfo.getProviderId());

        if (oAuthInfo.isEmpty()) {
            String signupToken = createTemporalSignupToken(kakaoUserInfo.getProviderId(),
                SocialProvider.KAKAO);
            return AuthResult.signupNeeded(AuthStatus.SIGNUP_NEEDED, signupToken);
        }

        TokenPair tokenPair = jwtTokenFactory.generateTokenPair(oAuthInfo.get().getUser());
        return AuthResult.success(AuthStatus.LOGIN_SUCCESS, oAuthInfo.get().getUser(), tokenPair);
    }

    public String createTemporalSignupToken(String providerId, SocialProvider socialProvider) {
        String temporalTokenId = UUID.randomUUID().toString();
        SignupToken signupToken = new SignupToken(temporalTokenId, providerId, null,
            socialProvider);
        signupTokenRepository.save(signupToken);
        return jwtTokenFactory.generateSignupTemporalToken(
            providerId,
            SocialProvider.KAKAO,
            temporalTokenId
        );
    }

    private DecodedJWT decodeJwtOrThrow(String jwt) {
        try {
            return JWT.decode(jwt);
        } catch (JWTDecodeException e) {
            throw UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID);
        }
    }
}
