package org.runimo.runimo.user.controller.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "사랑의 포인트 사용 요청",
    example = """
        {
          "love_point_amount": 100
        }
        """)
public record UseLovePointRequest(
    @Schema(description = "사용할 사랑의 포인트", example = "100")
    Long lovePointAmount
) {

}
