package org.runimo.runimo.auth.test;

import io.swagger.v3.oas.annotations.media.Schema;
import org.runimo.runimo.auth.service.dto.TokenPair;

@Schema(description = "테스트용 인증 응답 DTO")
public record TestAuthResponse(
    TokenPair tokens
) {

}
