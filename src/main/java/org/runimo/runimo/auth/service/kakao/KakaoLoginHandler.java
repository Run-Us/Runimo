package org.runimo.runimo.auth.service.kakao;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.UnRegisteredUserException;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.apple.KakaoUserInfo;
import org.runimo.runimo.auth.service.dtos.AuthResponse;
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

    @Transactional(readOnly = true)
    public AuthResponse validateAndLogin(final String rawToken) {
        DecodedJWT token;
        try {
            token = JWT.decode(rawToken);
        } catch (JWTDecodeException e) {
            throw UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID);
        }
        KakaoUserInfo kakaoUserInfo = kakaoTokenVerifier.verifyToken(token);
        OAuthInfo oAuthInfo = oAuthInfoRepository.findByProviderAndProviderId(SocialProvider.KAKAO,
                kakaoUserInfo.getProviderId())
            .orElseThrow(() -> UnRegisteredUserException.of(
                UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN,
                jwtTokenFactory.generateRegisterTemporalToken(kakaoUserInfo.getProviderId(),
                    SocialProvider.KAKAO)));
        TokenPair tokenPair = jwtTokenFactory.generateTokenPair(oAuthInfo.getUser());
        return new AuthResponse(oAuthInfo.getUser(), tokenPair);
    }
}
