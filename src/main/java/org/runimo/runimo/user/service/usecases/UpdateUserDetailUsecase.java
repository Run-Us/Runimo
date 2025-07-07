package org.runimo.runimo.user.service.usecases;

import org.runimo.runimo.user.service.dto.command.UpdateNotificationAllowedCommand;

public interface UpdateUserDetailUsecase {

    void updateUserNotificationAllowed(UpdateNotificationAllowedCommand command);
}
