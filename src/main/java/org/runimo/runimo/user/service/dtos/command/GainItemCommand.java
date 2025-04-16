package org.runimo.runimo.user.service.dtos.command;

public record GainItemCommand(
    Long userId,
    Long itemId,
    Long quantity
) {

}
