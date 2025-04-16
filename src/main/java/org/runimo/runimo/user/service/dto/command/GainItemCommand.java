package org.runimo.runimo.user.service.dto.command;

public record GainItemCommand(
    Long userId,
    Long itemId,
    Long quantity
) {

}
