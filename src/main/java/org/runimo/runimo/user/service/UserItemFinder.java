package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserItemFinder {

  private final UserItemRepository userItemRepository;

  @Transactional(readOnly = true)
  public Optional<UserItem> findEggByUserIdAndEggType(Long userId, EggType eggType) {
    return userItemRepository.findByUserIdAndEggType(userId, eggType);
  }

  @Transactional(readOnly = true)
  public Optional<UserItem> findByUserIdAndItemId(Long userId, Long itemId) {
    return userItemRepository.findByUserIdAndItemId(userId, itemId);
  }

  @Transactional
  public Optional<UserItem> findByUserIdAndItemIdWithXLock(Long userId, Long itemId) {
    return userItemRepository.findByUserIdAndItemIdForUpdate(userId, itemId);
  }
}
