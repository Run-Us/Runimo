package org.runimo.runimo.user.service.usecases;

public interface UpdateUserDetailUsecase {

    void updateUserNotificationAllowed(Long userId, boolean allowed);
}
