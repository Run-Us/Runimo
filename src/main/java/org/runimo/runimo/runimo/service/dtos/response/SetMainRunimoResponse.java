package org.runimo.runimo.runimo.service.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대표 러니모 등록 응답")
public record SetMainRunimoResponse(
    @Schema(description = "대표 러니모로 설정된 러니모 id", example = "0")
    Long mainRunimoId
) {

}
