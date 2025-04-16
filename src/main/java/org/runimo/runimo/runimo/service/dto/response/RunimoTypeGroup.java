package org.runimo.runimo.runimo.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전체 러니모 종류 조회 응답")
public record RunimoTypeGroup(
    @Schema(description = "러니모가 태어난 알 속성", example = "마당")
    String eggType,

    @Schema(description = "해당 알에서 태어나는 러니모 목록")
    List<RunimoTypeInfo> runimoTypes
) {

}
