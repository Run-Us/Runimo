package org.runimo.runimo.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 허용 여부 업데이트 요청")
public record UpdateNotificationAllowedRequst(
    @Schema(description = "알림 허용 여부", example = "true")
    boolean allowed,
    String deviceToken,
    String platform
) {

}
