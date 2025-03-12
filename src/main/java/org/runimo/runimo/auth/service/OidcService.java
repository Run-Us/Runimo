package org.runimo.runimo.auth.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.verifier.KakaoTokenVerifier;
import org.runimo.runimo.user.domain.SocialProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OidcService {

  private final KakaoTokenVerifier verifier;
  private final OidcNonceService nonceService;

  public String validateOidcTokenAndGetProviderId(final DecodedJWT token, final SocialProvider provider) {
    DecodedJWT verifyResult;
    // APPLE 로그인 추가 예정.
    switch (provider) {
      case KAKAO -> verifyResult = verifier.verifyToken(token);
      default -> throw new IllegalStateException("not supported provider");
    }
    nonceService.checkNonceAndSave(provider, verifyResult);
    return verifyResult.getSubject();
  }
}
