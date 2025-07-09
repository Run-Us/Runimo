package org.runimo.runimo.user.service.usecases.query;

import org.runimo.runimo.user.service.dto.response.NotificationAllowedResponse;

public interface UserInfoQueryUsecase {

    NotificationAllowedResponse getUserNotificationAllowed(Long userId);

}
