package org.runimo.runimo.hatch.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알 부화 요청")
public record HatchEggResponse(
    @Schema(description = "부화 결과 생성된 러니모 id", example = "1")
    Long id,

    @Schema(description = "부화 결과 생성된 러니모 이름", example = "토끼")
    String name,

    @Schema(description = "부화 결과 생성된 러니모 이미지 url", example = "http://...")
    String imgUrl,

    @Schema(description = "부화 결과 생성된 러니모 code", example = "R-101")
    String code,

    @Schema(description = "부화 결과 생성된 러니모를 이미 보유중인지", example = "true")
    Boolean isDuplicated
) {

}
