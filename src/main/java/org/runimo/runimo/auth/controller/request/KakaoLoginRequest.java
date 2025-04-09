package org.runimo.runimo.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 로그인 요청")
public record KakaoLoginRequest(
    @Schema(description = "카카오 oidc토큰")
    String oidcToken
) {

}
