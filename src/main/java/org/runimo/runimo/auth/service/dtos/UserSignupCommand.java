package org.runimo.runimo.auth.service.dtos;


public record UserSignupCommand(
    String registerToken,
    String nickname,
    String imgUrl
) {

}
