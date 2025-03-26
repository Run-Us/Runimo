package org.runimo.runimo.user.service.usecases;


import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.repository.MyItemRepository;
import org.runimo.runimo.user.service.dtos.InventoryItem;
import org.runimo.runimo.user.service.dtos.ItemQueryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyItemQueryUsecaseImpl implements MyItemQueryUsecase {

  private final MyItemRepository myItemRepository;

  @Override
  public ItemQueryResponse execute(Long userId) {
    List<InventoryItem> myItems = myItemRepository.findInventoryItemsByUserId(userId);
    return new ItemQueryResponse(myItems);
  }
}
