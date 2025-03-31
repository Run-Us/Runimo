package org.runimo.runimo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.dtos.TokenPair;

import java.util.Date;

public class JwtTokenFactory {

  private static final String ISSUER = "RUNIMO_SERVICE";

  private final String jwtSecret;
  private final long jwtExpiration;
  private final long jwtRefreshExpiration;

  public JwtTokenFactory(String jwtSecret, long jwtExpiration, long jwtRefreshExpiration) {
    this.jwtSecret = jwtSecret;
    this.jwtExpiration = jwtExpiration;
    this.jwtRefreshExpiration = jwtRefreshExpiration;
  }

  public String generateAccessToken(String userPublicId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);

    return JWT.create()
        .withSubject(userPublicId)
        .withIssuedAt(now)
        .withExpiresAt(expiryDate)
        .withIssuer(ISSUER)
        .sign(Algorithm.HMAC256(jwtSecret));
  }

  public String generateRefreshToken(String userPublicId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

    return JWT.create()
        .withSubject(userPublicId)
        .withIssuedAt(now)
        .withExpiresAt(expiryDate)
        .withIssuer(ISSUER)
        .withClaim("tokenType", "refresh")
        .sign(Algorithm.HMAC256(jwtSecret));
  }

  public TokenPair generateTokenPair(User user) {
    String accessToken = generateAccessToken(user.getPublicId());
    String refreshToken = generateRefreshToken(user.getPublicId());
    return new TokenPair(accessToken, refreshToken);
  }
}
