package org.runimo.runimo.user.controller.request;

import jakarta.validation.constraints.NotBlank;
import org.runimo.runimo.common.EnumValid;
import org.runimo.runimo.user.domain.SocialProvider;

public record AuthLoginRequest(
    @NotBlank String oidcToken,
    @NotBlank @EnumValid(enumClass = SocialProvider.class) String provider
) {
}
