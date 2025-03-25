package org.runimo.runimo.item.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 리워드로 지급되는 알의 생성을 담당하는 클래스
 * */
@Component
@RequiredArgsConstructor
public class EggFactory {

  private final ItemFinder itemFinder;
  private final EggTypeRandomGenerator eggTypeRandomGenerator;

  public Egg createGreetingEgg() {
    return itemFinder.findEggByEggType(EggType.MADANG)
        .orElseThrow(RuntimeException::new);
  }

  // 0 ~ 1 사이의 난수를 생성하여 그에 따라 알을 생성
  public Egg createEggRandomly(List<EggType> unLockedEggTypes) {
    EggType selectedType = eggTypeRandomGenerator.generateRandomEggType(unLockedEggTypes);
    return itemFinder.findEggByEggType(selectedType)
        .orElseThrow(RuntimeException::new);
  }
}
