package org.runimo.runimo.user.service.dtos;

import org.runimo.runimo.user.domain.SocialProvider;

public record UserRegisterCommand(
    String nickname,
    String imgUrl,
    String providerId,
    SocialProvider socialProvider
) {

}
