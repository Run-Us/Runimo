package org.runimo.runimo.user;

import org.runimo.runimo.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

public final class UserFixtures {

    public static User getDefaultUser() {
        return User.builder()
            .nickname("test")
            .imgUrl("test")
            .totalDistanceInMeters(0L)
            .totalTimeInSeconds(0L)
            .build();
    }

    public static User getUserWithId(Long id) {
        User user = User.builder()
            .nickname("test")
            .imgUrl("test")
            .totalDistanceInMeters(0L)
            .totalTimeInSeconds(0L)
            .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
