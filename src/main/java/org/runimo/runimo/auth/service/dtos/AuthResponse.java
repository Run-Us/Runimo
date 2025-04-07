package org.runimo.runimo.auth.service.dtos;

import org.runimo.runimo.user.domain.User;

public record AuthResponse(
    String nickname,
    String imgUrl,
    String accessToken,
    String refreshToken
) {

  public AuthResponse(final User user, final TokenPair tokenPair) {
    this(user.getNickname(),
        user.getImgUrl(),
        tokenPair.accessToken(),
        tokenPair.refreshToken());
  }
}
