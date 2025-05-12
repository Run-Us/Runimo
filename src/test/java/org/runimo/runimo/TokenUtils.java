package org.runimo.runimo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    private final static String AUTH_HEADER_PREFIX = "Bearer ";
    private final static Long TEST_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 30; // 30 days
    private final static String ISSUER = "RUNIMO_SERVICE";
    private final static String TEST_SECRET = "testSecret";
    @Autowired
    private JwtTokenFactory jwtTokenFactory;
    @Autowired
    private UserRepository userRepository;


    public String createTokenByUserPublicId(String publicId) {
        User user = userRepository.findByPublicId(publicId)
            .orElseThrow(() -> new IllegalStateException("테스트 유저가 존재하지 않습니다."));
        return AUTH_HEADER_PREFIX + jwtTokenFactory.generateAccessToken(user);
    }

    public String createTestOidcToken(String testPublicId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TEST_EXPIRATION_TIME);

        return JWT.create()
            .withSubject(testPublicId)
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .withIssuer(ISSUER)
            .sign(Algorithm.HMAC256(TEST_SECRET));
    }

    public static String createTestOnlyToken(String testPublicId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TEST_EXPIRATION_TIME);

        return AUTH_HEADER_PREFIX + JWT.create()
            .withSubject(testPublicId)
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .withIssuer(ISSUER)
            .withClaim("role", "USER")
            .sign(Algorithm.HMAC256(TEST_SECRET));
    }
}
