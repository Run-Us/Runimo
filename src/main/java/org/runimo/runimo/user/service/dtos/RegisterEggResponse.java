package org.runimo.runimo.user.service.dtos;

public record RegisterEggResponse(
    Long incubatingEggId,
    Long currentLovePointAmount,
    Long requiredLovePointAmount
) {
}
