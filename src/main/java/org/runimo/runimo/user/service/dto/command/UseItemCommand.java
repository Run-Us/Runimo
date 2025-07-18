package org.runimo.runimo.user.service.dto.command;

import org.runimo.runimo.item.domain.ActivityType;
import org.runimo.runimo.item.service.dto.CreateActivityCommand;

public record UseItemCommand(
    Long userId,
    Long itemId,
    Long quantity
) {

    public static CreateActivityCommand toItemUseActivityCommand(UseItemCommand command) {
        return new CreateActivityCommand(command.itemId(), command.userId(), command.quantity(),
            ActivityType.CONSUME);
    }
}
