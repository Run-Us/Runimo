package org.runimo.runimo.user.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserDeviceToken;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.exception.UserException;
import org.runimo.runimo.user.repository.UserDeviceTokenRepository;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dto.command.UpdateNotificationAllowedCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserDetailUsecaseImpl implements UpdateUserDetailUsecase {

    private final UserFinder userFinder;
    private final UserDeviceTokenRepository userDeviceTokenRepository;

    @Override
    @Transactional
    public void updateUserNotificationAllowed(UpdateNotificationAllowedCommand command) {
        UserDeviceToken token = userDeviceTokenRepository
            .findByUserId(command.userId())
            .orElseGet(() -> createUserDeviceTokenIfNotExist(command));
        token.updateDeviceToken(command.deviceToken());
        token.updateNotificationAllowed(command.allowed());
    }

    private UserDeviceToken createUserDeviceTokenIfNotExist(
        UpdateNotificationAllowedCommand command) {
        User user = userFinder.findUserById(command.userId())
            .orElseThrow(() -> UserException.of(UserHttpResponseCode.USER_NOT_FOUND));

        UserDeviceToken token = UserDeviceToken.builder()
            .user(user)
            .deviceToken(command.deviceToken())
            .platform(command.devicePlatform())
            .notificationAllowed(command.allowed())
            .build();
        return userDeviceTokenRepository.save(token);
    }
}
