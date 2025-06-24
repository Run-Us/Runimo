package org.runimo.runimo.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;
import org.runimo.runimo.user.domain.DevicePlatform;
import org.runimo.runimo.user.domain.Gender;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "사용자 회원가입 요청 DTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthSignupRequest {

    @Schema(description = "회원가입용 임시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...")
    @NotBlank(message = "회원가입 토큰은 필수입니다")
    private String registerToken;

    @Schema(description = "사용자 닉네임", example = "RunimoUser")
    @NotBlank
    String nickname;

    @Schema(description = "성별", example = "FEMALE")
    private Gender gender;

    @Schema(description = "디바이스 토큰", example = "string")
    private String deviceToken;

    @Schema(description = "디바이스 플랫폼", example = "FCM / APNS")
    private String devicePlatform;

    public AuthSignupRequest(String registerToken, String nickname, Gender gender) {
        this.registerToken = registerToken;
        this.nickname = nickname;
        this.gender = gender;
    }

    public UserSignupCommand toUserSignupCommand(MultipartFile file) {
        if (hasDeviceToken() && (devicePlatform == null || devicePlatform.trim().isEmpty())) {
            throw new IllegalArgumentException("디바이스 토큰이 있으면 플랫폼도 필수입니다.");
        }
        return new UserSignupCommand(
            registerToken,
            nickname,
            file,
            gender,
            deviceToken,
            devicePlatform != null ? DevicePlatform.fromString(devicePlatform) : null
        );
    }

    private boolean hasDeviceToken() {
        return deviceToken != null && !deviceToken.trim().isEmpty();
    }
}