package org.runimo.runimo.user.controller;

public record UseLovePointResponse(
    Long eggId,
    Long currentLovePointAmount,
    Long requiredLovePointAmount,
    Boolean eggHatchable
) {
}
