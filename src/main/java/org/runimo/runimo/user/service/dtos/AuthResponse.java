package org.runimo.runimo.user.service.dtos;

public record AuthResponse(
    String accessToken,
    String refreshToken
) {

  public AuthResponse(final TokenPair tokenPair) {
    this(tokenPair.accessToken(), tokenPair.refreshToken());
  }
}
