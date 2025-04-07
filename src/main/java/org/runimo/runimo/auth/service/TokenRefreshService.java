package org.runimo.runimo.auth.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

  private final JwtResolver jwtResolver;
  private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();
  private final JwtTokenFactory jwtTokenFactory;

  public TokenPair refreshAccessToken(String refreshToken) {
    String userId;
    try {
      jwtResolver.verifyJwtToken(refreshToken);
      userId = jwtResolver.getUserIdFromJwtToken(refreshToken);
    } catch (Exception e) {
      throw UserJwtException.of(UserHttpResponseCode.TOKEN_REFRESH_FAIL);
    }
    String storedToken = refreshTokenStore.get(userId);
    if (storedToken == null || !storedToken.equals(refreshToken)) {
      throw new IllegalArgumentException("Refresh token mismatch");
    }
    String newAccessToken = jwtTokenFactory.generateAccessToken(userId);
    String newRefreshToken = jwtTokenFactory.generateRefreshToken(userId);

    // 갱신한 리프레시 토큰 저장 (기존 토큰 갱신)
    refreshTokenStore.put(userId, newRefreshToken);
    return new TokenPair(newAccessToken, newRefreshToken);
  }

  public void storeRefreshToken(String userId, String refreshToken) {
    refreshTokenStore.put(userId, refreshToken);
  }
}
