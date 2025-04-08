package org.runimo.runimo.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.runimo.runimo.auth.service.dtos.UserSignupCommand;
import org.runimo.runimo.user.domain.Gender;

@Schema(description = "사용자 회원가입 요청 DTO")
public record AuthSignupRequest(
    @Schema(description = "회원가입용 임시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...")
    @NotBlank String registerToken,

    @Schema(description = "사용자 닉네임", example = "RunimoUser")
    @NotBlank String nickname,

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/image.jpg")
    @URL String imgUrl,

    @Schema(description = "성별", example = "FEMALE")
    Gender gender
) {

    public UserSignupCommand toUserSignupCommand() {
        return new UserSignupCommand(registerToken, nickname, imgUrl, gender);
    }
}
