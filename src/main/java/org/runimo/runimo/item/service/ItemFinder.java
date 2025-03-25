package org.runimo.runimo.item.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.item.domain.Item;
import org.runimo.runimo.item.repository.ItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemFinder {

  private final ItemRepository itemRepository;

  public Optional<Item> findById(Long itemId) {
    return itemRepository.findById(itemId);
  }

  public Optional<Egg> findEggByEggType(EggType eggtype) {
    return itemRepository.findByEggType(eggtype);
  }

  public Boolean isItemExist(Long itemId) {
    return itemRepository.existsById(itemId);
  }
}
