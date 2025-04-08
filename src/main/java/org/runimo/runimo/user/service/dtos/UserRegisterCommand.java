package org.runimo.runimo.user.service.dtos;

import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;

public record UserRegisterCommand(
    String nickname,
    String imgUrl,
    Gender gender,
    String providerId,
    SocialProvider socialProvider
) {

}
