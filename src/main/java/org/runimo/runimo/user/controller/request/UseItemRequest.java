package org.runimo.runimo.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "아이템 사용 요청 DTO")
public record UseItemRequest(
    @Schema(description = "아이템 ID", example = "1")
    Long itemId,
    @Schema(description = "수량", example = "1")
    Long quantity
) {
}
