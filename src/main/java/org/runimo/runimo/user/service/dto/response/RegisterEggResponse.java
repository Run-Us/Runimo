package org.runimo.runimo.user.service.dto.response;

public record RegisterEggResponse(
    Long incubatingEggId,
    Long currentLovePointAmount,
    Long requiredLovePointAmount
) {

}
