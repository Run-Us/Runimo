package org.runimo.runimo.user.service.dtos.command;

public record UseLovePointCommand(Long userId, Long incubatingEggId, Long lovePoint) {

}
