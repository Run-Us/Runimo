package org.runimo.runimo.auth.service.login.kakao;

import lombok.Getter;

@Getter
public class KakaoUserInfo {

    private final String providerId;

    public KakaoUserInfo(String providerId) {
        this.providerId = providerId;
    }
}
