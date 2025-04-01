package org.runimo.runimo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtResolver {

  private static final String ISSUER = "RUNIMO_SERVICE";
  private final String jwtSecret;

  public JwtResolver(String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  public DecodedJWT verifyAccessToken(String token) throws JWTVerificationException {
    return JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer(ISSUER).build().verify(token);
  }

  public String getUserIdFromAccessToken(String token) throws JWTVerificationException {
    DecodedJWT jwt = verifyAccessToken(token);
    return jwt.getSubject();
  }
}
