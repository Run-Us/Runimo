package org.runimo.runimo.auth.service.dtos;

import org.runimo.runimo.user.domain.User;

public record AuthResult(
    AuthStatus status,
    String nickname,
    String imgUrl,
    String accessToken,
    String refreshToken,
    String registerToken) {

    public AuthResult(final AuthStatus staus, final User user, final TokenPair tokenPair,
        final String registerToken) {
        this(
            staus,
            user.getNickname(),
            user.getImgUrl(),
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            registerToken);
    }

    public static AuthResult success(final AuthStatus status, final User user,
        final TokenPair tokenPair) {
        return new AuthResult(
            status,
            user,
            tokenPair,
            null);
    }

    public static AuthResult signupNeeded(final AuthStatus status, final String registerToken) {
        return new AuthResult(
            status,
            null,
            null,
            null,
            null,
            registerToken);
    }
}
