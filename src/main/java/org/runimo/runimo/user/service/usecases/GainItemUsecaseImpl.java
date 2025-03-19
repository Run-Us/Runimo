package org.runimo.runimo.user.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dtos.GainItemCommand;
import org.runimo.runimo.user.service.dtos.GainItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GainItemUsecaseImpl implements GainItemUsecase {

  private final UserItemFinder userItemFinder;
  private final UserItemRepository userItemRepository;

  @Override
  @Transactional
  public GainItemResponse gainItem(GainItemCommand command) {
    UserItem userItem = userItemFinder.findByUserIdAndItemIdWithXLock(command.userId(), command.itemId())
            .orElseThrow(NoSuchElementException::new);
    userItem.gainItem(command.quantity());
    userItemRepository.save(userItem);
    return new GainItemResponse(
        userItem.getItemId(),
        userItem.getQuantity()
    );
  }
}
