package org.runimo.runimo.user.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 허용 여부 응답")
public record NotificationAllowedResponse(
    @Schema(description = "사용자 ID", example = "1")
    Long userId,
    @Schema(description = "알림 허용 여부", example = "true")
    boolean allowed
) {

}
