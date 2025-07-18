package org.runimo.runimo.item.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;
import org.springframework.stereotype.Component;

/*
 * 리워드로 지급되는 알의 생성을 담당하는 클래스
 * */
@Component
@RequiredArgsConstructor
public class EggFactory {

    private static final Long GREETING_EGG_ID = 1L;
    private final ItemFinder itemFinder;
    private final EggTypeRandomGenerator eggTypeRandomGenerator;

    public Egg createGreetingEgg() {
        return (Egg) itemFinder.findById(GREETING_EGG_ID)
            .orElseThrow(RuntimeException::new);
    }

    // 0 ~ 1 사이의 난수를 생성하여 그에 따라 알을 생성
    public Egg createEggRandomly(List<EggType> unLockedEggTypes) {
        EggType selectedType = eggTypeRandomGenerator.generateRandomEggType(unLockedEggTypes);
        return itemFinder.findEggByEggType(selectedType)
            .orElseThrow(RuntimeException::new);
    }
}
