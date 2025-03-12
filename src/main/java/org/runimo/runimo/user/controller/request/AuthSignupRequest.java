package org.runimo.runimo.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.runimo.runimo.common.EnumValid;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;

@Schema(description = "사용자 회원가입 요청 DTO")
public record AuthSignupRequest(

    @Schema(description = "OIDC ID 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...")
    @NotBlank String oidcToken,

    @Schema(description = "소셜 로그인 제공자 (APPLE, KAKAO)", example = "APPLE")
    @NotBlank @EnumValid(enumClass = SocialProvider.class) String provider,

    @Schema(description = "사용자 닉네임", example = "RunimoUser")
    @NotBlank String nickname,

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/image.jpg")
    @URL String imgUrl
) {
  public UserSignupCommand toUserSignupCommand() {
    return new UserSignupCommand(nickname, SocialProvider.valueOf(provider), imgUrl);
  }
}
