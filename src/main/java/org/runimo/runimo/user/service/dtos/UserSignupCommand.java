package org.runimo.runimo.user.service.dtos;

import org.runimo.runimo.user.domain.SocialProvider;

public record UserSignupCommand(
    String nickname,
    SocialProvider provider,
    String imgUrl
) {
}
