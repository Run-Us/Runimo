package org.runimo.runimo.user.service.dto.command;

public record RegisterEggCommand(
    Long userId,
    Long itemId
) {

}
