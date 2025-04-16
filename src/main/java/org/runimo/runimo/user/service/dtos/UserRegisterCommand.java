package org.runimo.runimo.user.service.dtos;

import jakarta.validation.constraints.NotNull;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;

public record UserRegisterCommand(
    String nickname,
    String imgUrl,
    Gender gender,
    @NotNull String providerId,
    @NotNull SocialProvider socialProvider
) {

}
