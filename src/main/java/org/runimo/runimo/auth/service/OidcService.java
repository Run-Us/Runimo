package org.runimo.runimo.auth.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.service.dto.AuthResult;
import org.runimo.runimo.auth.service.login.apple.AppleLoginHandler;
import org.runimo.runimo.auth.service.login.kakao.KakaoLoginHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OidcService {

    private final AppleLoginHandler appleLoginHandler;
    private final KakaoLoginHandler kakaoLoginHandler;

    public AuthResult kakaoLogin(String idToken) {
        return kakaoLoginHandler.validateAndLogin(idToken);
    }

    public AuthResult appleLogin(String authCode, String codeVerifier) {
        return appleLoginHandler.validateAndLogin(authCode, codeVerifier);
    }
}
