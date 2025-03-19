package org.runimo.runimo.user.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dtos.GainItemCommand;
import org.runimo.runimo.user.service.dtos.GainItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GainItemUsecaseImpl implements GainItemUsecase {

  private final UserItemFinder userItemFinder;
  private final UserItemRepository userItemRepository;

  @Override
  @Transactional
  public GainItemResponse gainItem(GainItemCommand command) {
    UserItem userItem = userItemFinder.findByUserIdAndItemId(command.userId(), command.itemId())
        .orElse(new UserItem(command.userId(), command.itemId(), 0L));
    userItem.gainItem(command.quantity());
    userItemRepository.save(userItem);
    return new GainItemResponse(
        userItem.getItemId(),
        userItem.getQuantity()
    );
  }
}
