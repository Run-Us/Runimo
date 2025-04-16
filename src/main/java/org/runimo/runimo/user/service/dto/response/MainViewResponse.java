package org.runimo.runimo.user.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.runimo.runimo.runimo.service.dto.MainRunimoStat;
import org.runimo.runimo.user.service.dto.UserMainViewInfo;

@Schema(description = "메인화면 응답")
public record MainViewResponse(
    @Schema(description = "대표 러니모 정보 - 등록안하면 Null")
    MainRunimoStat mainRunimoStatNullable,
    @Schema(description = "사용자 정보")
    UserMainViewInfo userInfo
) {

}
