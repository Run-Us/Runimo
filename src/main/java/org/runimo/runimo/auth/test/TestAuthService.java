package org.runimo.runimo.auth.test;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.SignUpException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.UserRepository;
import org.runimo.runimo.user.service.UserCreator;
import org.runimo.runimo.user.service.UserItemCreator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile({"test", "dev"})
@Service
@RequiredArgsConstructor
public class TestAuthService {

    private final UserRepository userRepository;
    private final JwtTokenFactory jwtTokenFactory;
    private final UserCreator userCreator;
    private final UserItemCreator userItemCreator;
    private final EggGrantService eggGrantService;
    private final TokenRefreshService tokenRefreshService;


    @Transactional
    public TestAuthResponse login(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> SignUpException.of(UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN));
        TokenPair pair = jwtTokenFactory.generateTokenPair(user);
        tokenRefreshService.putRefreshToken(user.getPublicId(), pair.refreshToken());
        return new TestAuthResponse(pair);
    }

    @Transactional
    public TestAuthResponse signUp() {
        User testUser = User.builder()
            .nickname("test-user-" + UUID.randomUUID())
            .gender(Gender.UNKNOWN)
            .totalTimeInSeconds(0L)
            .totalDistanceInMeters(0L)
            .build();
        userRepository.save(testUser);

        userCreator.createLovePoint(testUser.getId());
        userItemCreator.createAll(testUser.getId());
        eggGrantService.grantGreetingEggToUser(testUser);

        return new TestAuthResponse(jwtTokenFactory.generateTokenPair(testUser));
    }
}
