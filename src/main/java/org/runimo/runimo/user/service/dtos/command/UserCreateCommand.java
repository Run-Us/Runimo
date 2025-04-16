package org.runimo.runimo.user.service.dtos.command;

import org.runimo.runimo.user.domain.Gender;

public record UserCreateCommand(
    String nickname,
    String imgUrl,
    Gender gender
) {

}
