package org.runimo.runimo.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.runimo.runimo.common.EnumValid;
import org.runimo.runimo.user.domain.SocialProvider;

@Schema(description = "사용자 로그인 요청 DTO")
public record AuthLoginRequest(
    @Schema(description = "OIDC ID 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...")
    @NotBlank String oidcToken,
    @Schema(description = "소셜 로그인 제공자 (APPLE, KAKAO)", example = "APPLE")
    @NotBlank @EnumValid(enumClass = SocialProvider.class) String provider
) {
}
