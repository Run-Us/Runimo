package org.runimo.runimo.user.service.dto.command;

public record UseLovePointCommand(Long userId, Long incubatingEggId, Long lovePoint) {

}
