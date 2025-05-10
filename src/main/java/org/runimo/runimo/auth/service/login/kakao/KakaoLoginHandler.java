package org.runimo.runimo.auth.service.login.kakao;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.login.OIDCLoginHandler;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.springframework.stereotype.Component;

@Component
public class KakaoLoginHandler extends OIDCLoginHandler {

    private final KakaoTokenVerifier kakaoTokenVerifier;

    public KakaoLoginHandler(
        KakaoTokenVerifier kakaoTokenVerifier,
        JwtTokenFactory jwtTokenFactory,
        TokenRefreshService tokenRefreshService,
        OAuthInfoRepository oAuthInfoRepository,
        SignupTokenRepository signupTokenRepository) {
        super(jwtTokenFactory, tokenRefreshService, oAuthInfoRepository, signupTokenRepository);
        this.kakaoTokenVerifier = kakaoTokenVerifier;
    }

    @Override
    protected String extractProviderId(Object... params) {
        if (params.length < 1 || !(params[0] instanceof String idToken)) {
            throw new IllegalArgumentException("Kakao login requires an ID token parameter");
        }

        DecodedJWT jwt = decodeJwtOrThrow(idToken);
        KakaoUserInfo userInfo = kakaoTokenVerifier.verifyToken(jwt);
        return userInfo.getProviderId();
    }

    @Override
    protected SignupToken createProviderSpecificToken(String tokenId, String providerId,
        Object... params) {
        return new SignupToken(tokenId, providerId, null, SocialProvider.KAKAO);
    }

    @Override
    protected SocialProvider getProviderType() {
        return SocialProvider.KAKAO;
    }

    private DecodedJWT decodeJwtOrThrow(String jwt) {
        try {
            return JWT.decode(jwt);
        } catch (JWTDecodeException e) {
            throw UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID);
        }
    }
}
