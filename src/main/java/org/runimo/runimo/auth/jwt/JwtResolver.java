package org.runimo.runimo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;

public class JwtResolver {

    private static final String ISSUER = "RUNIMO_SERVICE";
    private final String jwtSecret;

    public JwtResolver(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public DecodedJWT verifyJwtToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer(ISSUER).build().verify(token);
    }

    public String getUserIdFromJwtToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = verifyJwtToken(token);
        return jwt.getSubject();
    }

    public RegisterTokenPayload getRegisterTokenPayload(String token)
        throws JWTVerificationException {
        try {
            DecodedJWT decodedJWT = verifyJwtToken(token);
            String providerId = decodedJWT.getClaim("provider_id").asString();
            SocialProvider socialProvider = SocialProvider.valueOf(
                decodedJWT.getClaim("provider").asString()
            );
            return new RegisterTokenPayload(providerId, socialProvider);
        } catch (JWTVerificationException e) {
            throw UserJwtException.of(UserHttpResponseCode.TOKEN_INVALID);
        }
    }


}
