package org.runimo.runimo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;

import java.util.Date;
import java.util.UUID;

public class JwtTokenFactory {

  private static final String ISSUER = "RUNIMO_SERVICE";

  private final String jwtSecret;
  private final long jwtExpiration;
  private final long jwtRefreshExpiration;
  private final long tempJwtExpiration;

  public JwtTokenFactory(String jwtSecret, long jwtExpiration, long jwtRefreshExpiration, long tempJwtExpiration) {
    this.jwtSecret = jwtSecret;
    this.jwtExpiration = jwtExpiration;
    this.jwtRefreshExpiration = jwtRefreshExpiration;
    this.tempJwtExpiration = tempJwtExpiration;
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

  public String generateRegisterTemporalToken(String providerId, SocialProvider socialProvider) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tempJwtExpiration);
    return JWT.create()
        .withSubject(UUID.randomUUID().toString())
        .withIssuedAt(now)
        .withExpiresAt(expiryDate)
        .withIssuer(ISSUER)
        .withClaim("provider_id", providerId)
        .withClaim("provider", socialProvider.name())
        .withClaim("tokenType", "register")
        .sign(Algorithm.HMAC256(jwtSecret));
  }

  public TokenPair generateTokenPair(User user) {
    String accessToken = generateAccessToken(user.getPublicId());
    String refreshToken = generateRefreshToken(user.getPublicId());
    return new TokenPair(accessToken, refreshToken);
  }
}
