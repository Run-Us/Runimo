package org.runimo.runimo.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.runimo.runimo.user.repository.IncubatingEggRepository;
import org.runimo.runimo.user.service.dtos.IncubatingEggView;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class IncubatingEggFinder {

    private final IncubatingEggRepository incubatingEggRepository;

    @Transactional(readOnly = true)
    public List<IncubatingEgg> findIncubatingEggsByUserId(Long userId) {
        return incubatingEggRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<IncubatingEggView> findIncubatingEggsViewByUserId(Long userId) {
        return incubatingEggRepository.findAllViewByUserId(userId);
    }
}
