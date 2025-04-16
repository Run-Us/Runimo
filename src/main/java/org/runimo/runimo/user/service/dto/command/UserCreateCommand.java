package org.runimo.runimo.user.service.dto.command;

import org.runimo.runimo.user.domain.Gender;

public record UserCreateCommand(
    String nickname,
    String imgUrl,
    Gender gender
) {

}
