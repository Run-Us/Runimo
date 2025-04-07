package org.runimo.runimo.auth.service.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.UnRegisteredUserException;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.dtos.AuthResponse;
import org.runimo.runimo.auth.service.dtos.TokenPair;
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

  @Transactional(readOnly = true)
  public AuthResponse validateAndLogin(final String authCode, final String verifier) {
    String rawToken = appleTokenVerifier.getAccessTokenFromAuthCode(authCode, verifier);
    DecodedJWT decodedJWT;
    try {
      decodedJWT = JWT.decode(rawToken);
    } catch (JWTDecodeException e) {
      throw UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID);
    }
    AppleUserInfo userInfo = appleTokenVerifier.verifyToken(decodedJWT);
    OAuthInfo savedUser = oAuthInfoRepository.findByProviderAndProviderId(
            SocialProvider.APPLE,
            userInfo.getProviderId())
        .orElseThrow(() ->
            UnRegisteredUserException.of(
                UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN,
                jwtTokenFactory.generateRegisterTemporalToken(userInfo.getProviderId(), SocialProvider.APPLE))
        );
    TokenPair tokenPair = jwtTokenFactory.generateTokenPair(savedUser.getUser());
    return new AuthResponse(savedUser.getUser(), tokenPair);
  }
}
