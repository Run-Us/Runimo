package org.runimo.runimo.runimo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전체 러니모 종류 조회 응답")
public record GetRunimoTypeListResponse(
    List<RunimoTypeInfo> runimoTypes
) {

}
