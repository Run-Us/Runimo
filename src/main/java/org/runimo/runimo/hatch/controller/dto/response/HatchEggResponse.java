package org.runimo.runimo.hatch.controller.dto.response;

public record HatchEggResponse(
    String name,
    String imgUrl,
    String code,
    Boolean isDuplicated
) {
}
