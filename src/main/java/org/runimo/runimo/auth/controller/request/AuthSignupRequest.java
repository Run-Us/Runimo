package org.runimo.runimo.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;
import org.runimo.runimo.user.domain.Gender;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "사용자 회원가입 요청 DTO")
public record AuthSignupRequest(
    @Schema(description = "회원가입용 임시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...")
    @NotBlank String registerToken,

    @Schema(description = "사용자 닉네임", example = "RunimoUser")
    @NotBlank String nickname,

    @Schema(description = "성별", example = "FEMALE")
    Gender gender
) {

    public UserSignupCommand toUserSignupCommand(MultipartFile file) {
        return new UserSignupCommand(registerToken, nickname, file, gender);
    }
}
