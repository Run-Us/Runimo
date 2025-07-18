package org.runimo.runimo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;

public class JwtTokenFactory {

    private static final String ISSUER = "RUNIMO_SERVICE";

    private final String jwtSecret;
    private final long jwtExpiration;
    private final long jwtRefreshExpiration;
    private final long tempJwtExpiration;

    public JwtTokenFactory(String jwtSecret, long jwtExpiration, long jwtRefreshExpiration,
        long tempJwtExpiration) {
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
        this.jwtRefreshExpiration = jwtRefreshExpiration;
        this.tempJwtExpiration = tempJwtExpiration;
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return JWT.create()
            .withSubject(user.getPublicId())
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .withIssuer(ISSUER)
            .withClaim("role", user.getRole().name())
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

    public String generateSignupTemporalToken(String providerId, SocialProvider socialProvider,
        String key) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tempJwtExpiration);
        return JWT.create()
            .withSubject(key)
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .withIssuer(ISSUER)
            .withClaim("provider_id", providerId)
            .withClaim("provider", socialProvider.name())
            .withClaim("tokenType", "register")
            .sign(Algorithm.HMAC256(jwtSecret));
    }

    public TokenPair generateTokenPair(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user.getPublicId());
        return new TokenPair(accessToken, refreshToken);
    }
}
