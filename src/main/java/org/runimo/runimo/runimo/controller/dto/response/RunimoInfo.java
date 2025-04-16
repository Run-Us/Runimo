package org.runimo.runimo.runimo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RunimoInfo(
    @Schema(description = "러니모 id", example = "0")
    Long id,

    @Schema(description = "러니모 code", example = "R-101")
    String code,

    @Schema(description = "함께한 러닝 누적 횟수", example = "0")
    Long totalRunCount,

    @Schema(description = "함께한 러닝 누적 거리", example = "0")
    Long totalDistanceInMeters,

    @Schema(description = "메인 러니모 여부", example = "true")
    Boolean isMainRunimo
) {

}
