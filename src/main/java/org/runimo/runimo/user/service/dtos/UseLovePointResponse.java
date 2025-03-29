package org.runimo.runimo.user.service.dtos;

public record UseLovePointResponse(
    Long eggId,
    Long currentLovePointAmount,
    Long requiredLovePointAmount,
    Boolean eggHatchable
) {
}
