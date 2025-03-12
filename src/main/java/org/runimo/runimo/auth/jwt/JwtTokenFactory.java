package org.runimo.runimo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.dtos.TokenPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenFactory {

  private static final String ISSUER = "RUNIMO_SERVICE";

  @Value("${jwt.secret}")
  private String jwtSecret;
  @Value("${jwt.expiration}")
  private long jwtExpiration;
  @Value("${jwt.refresh.expiration}")
  private long jwtRefreshExpiration;

  public String generateAccessToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);

    return JWT.create()
        .withSubject(user.getPublicId())
        .withIssuedAt(now)
        .withExpiresAt(expiryDate)
        .withIssuer(ISSUER)
        .sign(Algorithm.HMAC256(jwtSecret));
  }

  public String generateRefreshToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

    return JWT.create()
        .withSubject(user.getPublicId())
        .withIssuedAt(now)
        .withExpiresAt(expiryDate)
        .withIssuer(ISSUER)
        .withClaim("tokenType", "refresh")
        .sign(Algorithm.HMAC256(jwtSecret));
  }

  public TokenPair generateTokenPair(User user) {
    String accessToken = generateAccessToken(user);
    String refreshToken = generateRefreshToken(user);
    return new TokenPair(accessToken, refreshToken);
  }
}
