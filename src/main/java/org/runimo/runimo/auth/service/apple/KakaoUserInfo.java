package org.runimo.runimo.auth.service.apple;

import lombok.Getter;

@Getter
public class KakaoUserInfo {

    private final String providerId;

    public KakaoUserInfo(String providerId) {
        this.providerId = providerId;
    }
}
