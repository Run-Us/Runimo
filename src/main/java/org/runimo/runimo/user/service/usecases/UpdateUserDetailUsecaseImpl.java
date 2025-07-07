package org.runimo.runimo.user.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.UserDeviceToken;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.exception.UserException;
import org.runimo.runimo.user.repository.UserDeviceTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserDetailUsecaseImpl implements UpdateUserDetailUsecase {

    private final UserDeviceTokenRepository userDeviceTokenRepository;

    @Override
    public void updateUserNotificationAllowed(Long userId, boolean allowed) {
        UserDeviceToken userDeviceToken = userDeviceTokenRepository.findByUserId(userId)
            .orElseThrow(() -> UserException.of(UserHttpResponseCode.USER_NOT_FOUND));
        userDeviceToken.updateNotificationAllowed(allowed);
        userDeviceTokenRepository.save(userDeviceToken);
    }
}
