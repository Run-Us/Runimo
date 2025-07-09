package org.runimo.runimo.user.service.dto.command;

import org.runimo.runimo.user.controller.request.UpdateNotificationAllowedRequst;
import org.runimo.runimo.user.domain.DevicePlatform;

public record UpdateNotificationAllowedCommand(
    Long userId,
    Boolean allowed,
    String deviceToken,
    DevicePlatform devicePlatform
) {

    public static UpdateNotificationAllowedCommand of(Long userId,
        UpdateNotificationAllowedRequst request) {
        return new UpdateNotificationAllowedCommand(userId, request.allowed(),
            request.deviceToken(), DevicePlatform.fromString(request.platform()));
    }


}
