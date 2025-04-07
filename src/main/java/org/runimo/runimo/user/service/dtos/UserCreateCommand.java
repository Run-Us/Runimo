package org.runimo.runimo.user.service.dtos;

public record UserCreateCommand(
    String nickname,
    String imgUrl
) {
}
