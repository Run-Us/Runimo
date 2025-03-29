package org.runimo.runimo.user.service.dtos;

public record UseLovePointCommand(Long userId, Long incubatingEggId, Long lovePoint) {
}
