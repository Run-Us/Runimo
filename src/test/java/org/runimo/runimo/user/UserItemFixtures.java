package org.runimo.runimo.user;

import org.runimo.runimo.user.domain.UserItem;

public class UserItemFixtures {

  private static final Long DEFAULT_USER_ID = 1L;
  private static final Long DEFAULT_ITEM_ID = 1L;

  public static UserItem getUserItemWithQuantity(Long quantity) {
    return UserItem.builder()
        .userId(DEFAULT_USER_ID)
        .itemId(DEFAULT_ITEM_ID)
        .quantity(quantity)
        .build();
  }
}
