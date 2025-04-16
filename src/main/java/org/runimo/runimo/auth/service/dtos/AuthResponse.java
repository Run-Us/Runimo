package org.runimo.runimo.auth.service.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 성공 응답")
public record AuthResponse(
    String nickname,
    String imgUrl,
    String accessToken,
    String refreshToken
) {
}
