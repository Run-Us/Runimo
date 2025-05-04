package org.runimo.runimo.auth.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.exceptions.SignUpException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.jwt.SignupTokenPayload;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.dto.SignupUserResponse;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;
import org.runimo.runimo.external.FileStorageService;
import org.runimo.runimo.user.domain.AppleUserToken;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.AppleUserTokenRepository;
import org.runimo.runimo.user.service.UserRegisterService;
import org.runimo.runimo.user.service.dto.command.UserRegisterCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SignUpUsecaseImpl implements SignUpUsecase {

    private static final int REGISTER_CUTOFF_MIN = 10;
    private final UserRegisterService userRegisterService;
    private final FileStorageService fileStorageService;
    private final JwtTokenFactory jwtTokenFactory;
    private final SignupTokenRepository signupTokenRepository;
    private final AppleUserTokenRepository appleUserTokenRepository;
    private final JwtResolver jwtResolver;

    @Override
    @Transactional
    public SignupUserResponse register(UserSignupCommand command) {
        SignupTokenPayload payload = jwtResolver.getSignupTokenPayload(command.registerToken());
        SignupToken signupToken = findUnExpiredSignupToken(payload.token());
        userRegisterService.validateExistingUser(payload.providerId(), payload.socialProvider());
        String imgUrl = fileStorageService.storeFile(command.profileImage());
        User savedUser = userRegisterService.registerUser(new UserRegisterCommand(
            command.nickname(),
            imgUrl,
            command.gender(),
            payload.providerId(),
            payload.socialProvider())
        );
        if (payload.socialProvider() == SocialProvider.APPLE) {
            createAppleUserToken(savedUser.getId(), signupToken);
        }
        removeSignupToken(payload.token());
        return new SignupUserResponse(savedUser, jwtTokenFactory.generateTokenPair(savedUser));
    }

    private void removeSignupToken(String token) {
        signupTokenRepository.deleteByToken(token);
    }

    private SignupToken findUnExpiredSignupToken(String token) {
        LocalDateTime cutOffTime = LocalDateTime.now().minusMinutes(REGISTER_CUTOFF_MIN);
        return signupTokenRepository.
            findByIdAndCreatedAtAfter(token, cutOffTime)
            .orElseThrow(() -> new SignUpException(UserHttpResponseCode.TOKEN_INVALID));
    }

    private void createAppleUserToken(Long userId, SignupToken signupToken) {
        AppleUserToken appleUserToken = new AppleUserToken(
            userId,
            signupToken.getRefreshToken()
        );
        appleUserTokenRepository.save(appleUserToken);
    }
}
