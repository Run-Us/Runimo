package org.runimo.runimo.auth.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.common.cache.InMemoryCache;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

  private final JwtResolver jwtResolver;
  private final InMemoryCache<String, String> refreshTokenCache;
  private final JwtTokenFactory jwtTokenFactory;
  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpiry;

  public TokenPair refreshAccessToken(String refreshToken) {
    String userId;
    try {
      jwtResolver.verifyJwtToken(refreshToken);
      userId = jwtResolver.getUserIdFromJwtToken(refreshToken);
    } catch (Exception e) {
      throw UserJwtException.of(UserHttpResponseCode.TOKEN_REFRESH_FAIL);
    }

    String storedToken = refreshTokenCache.get(userId).orElse(null);
    if (storedToken == null || !storedToken.equals(refreshToken)) {
      throw new IllegalArgumentException("Refresh token mismatch");
    }

    String newAccessToken = jwtTokenFactory.generateAccessToken(userId);
    String newRefreshToken = jwtTokenFactory.generateRefreshToken(userId);

    // 갱신한 리프레시 토큰 저장 (기존 토큰 갱신)
    refreshTokenCache.put(userId, newRefreshToken, Duration.ofMillis(refreshTokenExpiry));
    return new TokenPair(newAccessToken, newRefreshToken);
  }
}
