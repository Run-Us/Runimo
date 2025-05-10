package org.runimo.runimo.auth.service.login.apple;

import lombok.Getter;

@Getter
public class AppleUserInfo {

    private final String providerId;
    private final String name;

    public AppleUserInfo(String providerId, String name) {
        this.providerId = providerId;
        this.name = name;
    }
}
