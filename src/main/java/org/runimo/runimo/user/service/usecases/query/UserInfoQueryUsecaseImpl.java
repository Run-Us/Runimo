package org.runimo.runimo.user.service.usecases.query;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.exception.UserException;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dto.response.NotificationAllowedResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoQueryUsecaseImpl implements UserInfoQueryUsecase {

    private final UserFinder userFinder;

    @Override
    public NotificationAllowedResponse getUserNotificationAllowed(Long userId) {
        if (userFinder.findUserById(userId).isEmpty()) {
            throw UserException.of(UserHttpResponseCode.USER_NOT_FOUND);
        }
        var userDeviceToken = userFinder.findUserDeviceTokenByUserId(userId)
            .orElseThrow(() -> UserException.of(UserHttpResponseCode.DEVICE_TOKEN_NOT_FOUND));
        return new NotificationAllowedResponse(userId, userDeviceToken.getNotificationAllowed());
    }
}
