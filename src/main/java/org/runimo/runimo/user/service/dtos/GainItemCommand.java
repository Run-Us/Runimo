package org.runimo.runimo.user.service.dtos;

public record GainItemCommand(
    Long userId,
    Long itemId,
    Long quantity
) {
}
