package org.runimo.runimo.user.service.dtos;

import org.runimo.runimo.user.domain.Gender;

public record UserCreateCommand(
    String nickname,
    String imgUrl,
    Gender gender
) {

}
