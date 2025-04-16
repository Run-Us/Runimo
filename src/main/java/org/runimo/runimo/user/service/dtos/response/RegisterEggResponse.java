package org.runimo.runimo.user.service.dtos.response;

public record RegisterEggResponse(
    Long incubatingEggId,
    Long currentLovePointAmount,
    Long requiredLovePointAmount
) {

}
