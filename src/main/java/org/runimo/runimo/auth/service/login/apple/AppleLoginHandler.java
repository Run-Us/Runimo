package org.runimo.runimo.auth.service.login.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.EncryptUtil;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.auth.service.login.OIDCLoginHandler;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.springframework.stereotype.Component;

@Component
public class AppleLoginHandler extends OIDCLoginHandler {

    private final AppleTokenVerifier appleTokenVerifier;
    private final EncryptUtil encryptUtil;

    public AppleLoginHandler(
        AppleTokenVerifier appleTokenVerifier,
        JwtTokenFactory jwtTokenFactory,
        TokenRefreshService tokenRefreshService,
        OAuthInfoRepository oAuthInfoRepository,
        SignupTokenRepository signupTokenRepository,
        EncryptUtil encryptUtil) {
        super(jwtTokenFactory, tokenRefreshService, oAuthInfoRepository, signupTokenRepository);
        this.appleTokenVerifier = appleTokenVerifier;
        this.encryptUtil = encryptUtil;
    }


    /**
     * 애플 로그인 검증을 처리하고 사용자를 로그인 또는 회원가입으로 안내합니다.
     *
     * @param params [iOS 앱에서 받은 oidc 인증 코드, iOS 앱에서 받은 PKCE용 Verifier]
     * @return 인증 결과를 담은 객체; 로그인 성공 시 사용자 정보와 토큰 쌍을 포함합니다. 실패시 회원가입 토큰을 반환합니다.
     */
    @Override
    protected String extractProviderId(Object... params) {
        if (params.length < 2 || !(params[0] instanceof String authCode)
            || !(params[1] instanceof String verifier)) {
            throw new IllegalArgumentException(
                "Apple login requires auth code and verifier parameters");
        }
        TokenPair appleTokens = appleTokenVerifier.getAppleTokenPairFromAuthCode(authCode,
            verifier);
        DecodedJWT jwt = decodeJwtOrThrow(appleTokens.accessToken());
        AppleUserInfo userInfo = appleTokenVerifier.verifyTokenAndExtractUserInfo(jwt);

        AppleAuthContext.setCurrentTokens(appleTokens);

        return userInfo.getProviderId();
    }

    @Override
    protected SignupToken createProviderSpecificToken(String tokenId, String providerId,
        Object... params) {
        TokenPair appleTokens = AppleAuthContext.getCurrentTokens();
        String encryptedRefreshToken = encryptUtil.encrypt(appleTokens.refreshToken());

        AppleAuthContext.clear();

        return new SignupToken(
            tokenId,
            providerId,
            encryptedRefreshToken,
            SocialProvider.APPLE
        );
    }

    @Override
    protected SocialProvider getProviderType() {
        return SocialProvider.APPLE;
    }

    private DecodedJWT decodeJwtOrThrow(String jwt) {
        try {
            return JWT.decode(jwt);
        } catch (JWTDecodeException e) {
            throw UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID);
        }
    }

}
