package org.runimo.runimo.user.service.dtos;

public record RegisterEggCommand(
    Long userId,
    Long itemId
) {

}
