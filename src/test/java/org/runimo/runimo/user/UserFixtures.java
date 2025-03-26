package org.runimo.runimo.user;

import org.runimo.runimo.user.domain.User;

public final class UserFixtures {

  public static User getDefaultUser() {
    return User.builder()
        .nickname("test")
        .imgUrl("test")
        .totalDistanceInMeters(0L)
        .totalTimeInSeconds(0L)
        .build();
  }
}
