package org.runimo.runimo.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "애플 로그인 요청")
public record AppleLoginRequest(
    @Schema(description = "인증 코드")
    @NotBlank String authCode,
    @Schema(description = "Code Verifier")
    @NotBlank String codeVerifier
) {
}
