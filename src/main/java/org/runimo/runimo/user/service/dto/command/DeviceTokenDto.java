package org.runimo.runimo.user.service.dto.command;

import org.runimo.runimo.user.domain.DevicePlatform;

public record DeviceTokenDto(String token, DevicePlatform platform) {

    public static final DeviceTokenDto EMPTY = new DeviceTokenDto("", null);

    public static DeviceTokenDto of(String deviceToken, DevicePlatform devicePlatform) {
        if (deviceToken == null || deviceToken.isEmpty()) {
            return EMPTY;
        }
        return new DeviceTokenDto(deviceToken, devicePlatform);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
