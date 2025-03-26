package org.runimo.runimo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtResolver {

  private static final String ISSUER = "RUNIMO_SERVICE";
  @Value("${jwt.secret}")
  private String jwtSecret;

  public DecodedJWT verifyAccessToken(String token) throws JWTVerificationException {
    return JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer(ISSUER).build().verify(token);
  }

  public String getUserIdFromAccessToken(String token) throws JWTVerificationException {
    DecodedJWT jwt = verifyAccessToken(token);
    return jwt.getSubject();
  }
}
