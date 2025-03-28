package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserItemProcessor {

  private final UserItemFinder userItemFinder;
  private final UserItemRepository userItemRepository;

  @Transactional
  public void updateItemQuantity(Long userId, Long itemId, Long amount) {
    UserItem userItem = userItemFinder.findByUserIdAndItemIdWithXLock(userId, itemId)
        .orElseThrow(IllegalStateException::new);
    userItem.gainItem(amount);
    userItemRepository.save(userItem);
  }
}
