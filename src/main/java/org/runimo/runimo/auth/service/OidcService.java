package org.runimo.runimo.auth.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.service.apple.AppleLoginHandler;
import org.runimo.runimo.auth.service.dtos.AuthResponse;
import org.runimo.runimo.auth.service.kakao.KakaoLoginHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OidcService {

  private final AppleLoginHandler appleLoginHandler;
  private final KakaoLoginHandler kakaoLoginHandler;

  public AuthResponse kakaoLogin(String idToken) {
    return kakaoLoginHandler.validateAndLogin(idToken);
  }

  public AuthResponse appleLogin(String authCode, String codeVerifier) {
    return appleLoginHandler.validateAndLogin(authCode, codeVerifier);
  }
}
