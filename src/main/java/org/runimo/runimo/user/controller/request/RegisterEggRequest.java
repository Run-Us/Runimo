package org.runimo.runimo.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "부화할 알 등록 요청",
    example = """
        {
          "item_id": 1
        }
        """)
public record RegisterEggRequest(
    @Schema(description = "부화할 알 ID (보유중인 알)", example = "1")
    Long itemId
) {
}
