package org.runimo.runimo.auth.service.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.UnRegisteredUserException;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.EncryptUtil;
import org.runimo.runimo.auth.service.dtos.AuthResponse;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.auth.service.kakao.AppleUserInfo;
import org.runimo.runimo.user.domain.AppleUserToken;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.AppleUserTokenRepository;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AppleLoginHandler {

    private final AppleTokenVerifier appleTokenVerifier;
    private final JwtTokenFactory jwtTokenFactory;
    private final OAuthInfoRepository oAuthInfoRepository;
    private final AppleUserTokenRepository appleUserTokenRepository;
    private final EncryptUtil encryptUtil;

    @Transactional
    public AuthResponse validateAndLogin(final String authCode, final String verifier) {
        TokenPair appleToken = appleTokenVerifier.getAccessTokenFromAuthCode(authCode, verifier);
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.decode(appleToken.accessToken());
        } catch (JWTDecodeException e) {
            throw UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID);
        }
        AppleUserInfo userInfo = appleTokenVerifier.verifyToken(decodedJWT);
        OAuthInfo savedUser = getOrThrowWithRegisterToken(userInfo);
        saveAppleRefreshToken(savedUser, appleToken);
        TokenPair tokenPair = jwtTokenFactory.generateTokenPair(savedUser.getUser());
        return new AuthResponse(savedUser.getUser(), tokenPair);
    }

    // 사용자 정보가 있으면 반환하고 없다면, 회원가입을 위한 토큰을 생성하여 예외를 던진다.
    private OAuthInfo getOrThrowWithRegisterToken(AppleUserInfo userInfo) {
        return oAuthInfoRepository.findByProviderAndProviderId(
                SocialProvider.APPLE,
                userInfo.getProviderId())
            .orElseThrow(() ->
                UnRegisteredUserException.of(
                    UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN,
                    jwtTokenFactory.generateRegisterTemporalToken(userInfo.getProviderId(),
                        SocialProvider.APPLE))
            );
    }

    // 애플에서 발급한 refresh token을 DB에 저장한다.
    private void saveAppleRefreshToken(OAuthInfo savedUser, TokenPair appleToken) {
        AppleUserToken appleUserToken = appleUserTokenRepository
            .findByUserId(savedUser.getUser().getId())
            .orElse(createEncryptedRefreshToken(savedUser.getId(), appleToken.refreshToken()));
        appleUserTokenRepository.save(appleUserToken);
    }

    private AppleUserToken createEncryptedRefreshToken(Long userId, String refreshToken) {
        try {
            return new AppleUserToken(
                userId,
                encryptUtil.encrypt(refreshToken));
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt refresh token", e);
        }
    }
}
