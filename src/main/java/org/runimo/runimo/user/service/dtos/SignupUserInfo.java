package org.runimo.runimo.user.service.dtos;

public record SignupUserInfo(
    Long userId,
    TokenPair tokenPair
) {
}
