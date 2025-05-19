package org.runimo.runimo.user;

import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

public final class UserFixtures {

    public static final String TEST_USER_NICKNAME = "test";
    public static final String TEST_USER_IMG_URL = "test";
    public static final TokenPair TEST_TOKEN_PAIR = new TokenPair(
        "test-access-token-1", "test-refresh-token-1"
    );

    public static User getDefaultUser() {
        return User.builder()
            .nickname(TEST_USER_NICKNAME)
            .imgUrl(TEST_USER_IMG_URL)
            .totalDistanceInMeters(0L)
            .totalTimeInSeconds(0L)
            .build();
    }

    public static User getUserWithId(Long id) {
        User user = User.builder()
            .nickname(TEST_USER_NICKNAME)
            .imgUrl(TEST_USER_IMG_URL)
            .totalDistanceInMeters(0L)
            .totalTimeInSeconds(0L)
            .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
