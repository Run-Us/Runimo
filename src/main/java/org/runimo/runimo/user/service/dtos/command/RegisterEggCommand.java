package org.runimo.runimo.user.service.dtos.command;

public record RegisterEggCommand(
    Long userId,
    Long itemId
) {

}
