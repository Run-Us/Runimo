package org.runimo.runimo.auth.service.dto;


import org.runimo.runimo.user.domain.Gender;

public record UserSignupCommand(
    String registerToken,
    String nickname,
    String imgUrl,
    Gender gender
) {

}
