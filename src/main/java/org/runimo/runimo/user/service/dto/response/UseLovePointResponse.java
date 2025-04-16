package org.runimo.runimo.user.service.dto.response;

public record UseLovePointResponse(
    Long eggId,
    Long currentLovePointAmount,
    Long requiredLovePointAmount,
    Boolean eggHatchable
) {

}
