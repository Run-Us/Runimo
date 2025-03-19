package org.runimo.runimo.item.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Item;
import org.runimo.runimo.item.repository.ItemRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemFinder {

  private final ItemRepository itemRepository;

  public Optional<Item> findById(Long itemId) {
    return itemRepository.findById(itemId);
  }

  public Boolean isItemExist(Long itemId) {
    return itemRepository.existsById(itemId);
  }
}
