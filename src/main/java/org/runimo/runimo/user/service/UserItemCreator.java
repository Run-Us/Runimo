package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.repository.ItemRepository;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserItemCreator {

  private final UserItemRepository userItemRepository;
  private final ItemRepository itemRepository;

  @Transactional
  public UserItem create(UserItem userItem) {
    return userItemRepository.save(userItem);
  }

  /*
   * 아이템ID, 유저ID의 모든 순서쌍을 저장한다.
   * 회원가입 시 실행된다.
   * */
  @Transactional
  public void createAll(Long userId) {
    List<Long> itemIds = itemRepository.findAllItemIds();
    userItemRepository.saveAll(
        itemIds.stream()
            .map(itemId -> new UserItem(userId, itemId, 0L))
            .toList());
  }
}
