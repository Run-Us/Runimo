package org.runimo.runimo.user.service.dto.command;

import jakarta.validation.constraints.NotNull;
import org.runimo.runimo.user.domain.DevicePlatform;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;

public record UserRegisterCommand(
    String nickname,
    String imgUrl,
    Gender gender,
    @NotNull String providerId,
    @NotNull SocialProvider socialProvider,
    String deviceToken,
    DevicePlatform devicePlatform
) {

}
