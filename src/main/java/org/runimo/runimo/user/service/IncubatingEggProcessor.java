package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.user.domain.EggStatus;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.runimo.runimo.user.repository.IncubatingEggRepository;
import org.runimo.runimo.user.service.dtos.UseLovePointCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class IncubatingEggProcessor {

    private final IncubatingEggRepository incubatingEggRepository;

    @Transactional
    public IncubatingEgg create(Long userId, Egg egg) {
        IncubatingEgg incubatingEgg = IncubatingEgg.builder()
            .userId(userId)
            .eggId(egg.getId())
            .currentLovePointAmount(0L)
            .hatchRequireAmount(egg.getHatchRequireAmount())
            .status(EggStatus.INCUBATING)
            .build();
        return incubatingEggRepository.save(incubatingEgg);
    }

    @Transactional
    public IncubatingEgg giveLovePoint(UseLovePointCommand useLovePointCommand) {
        IncubatingEgg incubatingEgg = incubatingEggRepository.findByUserIdAndEggIdForUpdate(
                useLovePointCommand.userId(),
                useLovePointCommand.incubatingEggId())
            .orElseThrow(() -> new IllegalArgumentException("Incubating egg not found"));
        incubatingEgg.gainLovePoint(useLovePointCommand.lovePoint());
        return incubatingEgg;
    }

}
