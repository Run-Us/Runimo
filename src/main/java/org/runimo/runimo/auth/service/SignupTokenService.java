package org.runimo.runimo.auth.service;


import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.exceptions.SignUpException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.SignupTokenPayload;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupTokenService {

    private static final int REGISTER_CUTOFF_MIN = 10;
    private final JwtResolver jwtResolver;
    private final SignupTokenRepository signupTokenRepository;

    public SignupTokenPayload extractPayload(String token) {
        return jwtResolver.getSignupTokenPayload(token);
    }

    public SignupToken findUnExpiredToken(String token) {
        LocalDateTime cutOffTime = LocalDateTime.now().minusMinutes(REGISTER_CUTOFF_MIN);
        return signupTokenRepository.
            findByIdAndCreatedAtAfter(token, cutOffTime)
            .orElseThrow(() -> new SignUpException(UserHttpResponseCode.TOKEN_INVALID));
    }

    public void invalidateSignupToken(SignupToken signupToken) {
        try {
            signupToken.markAsUsed();
            signupTokenRepository.save(signupToken);
        } catch (OptimisticLockingFailureException e) {
            throw new SignUpException(UserHttpResponseCode.SIGNIN_FAIL_ALREADY_EXIST);
        }
    }
}
