package org.runimo.runimo.auth.service.dtos;

import org.runimo.runimo.user.domain.User;

public record SignupUserResponse(
    Long userId,
    String nickname,
    String imgUrl,
    TokenPair tokenPair
) {

    public SignupUserResponse(final User user, final TokenPair tokenPair) {
        this(user.getId(),
            user.getNickname(),
            user.getImgUrl(),
            tokenPair);
    }
}
