package org.runimo.runimo.user.domain;

public enum DevicePlatform {
    FCM,
    APNS,
    NONE;


    public static DevicePlatform fromString(String value) {
        return DevicePlatform.valueOf(value.toUpperCase());
    }
}
