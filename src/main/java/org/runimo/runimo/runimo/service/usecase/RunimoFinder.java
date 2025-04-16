package org.runimo.runimo.runimo.service.usecase;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.runimo.repository.RunimoRepository;
import org.runimo.runimo.runimo.service.dtos.MainRunimoStat;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RunimoFinder {

    private final RunimoRepository runimoRepository;

    @Transactional(readOnly = true)
    public Optional<MainRunimoStat> findMainRunimoStatByUserId(Long mainRunimoId) {
        return runimoRepository.findMainRunimoStatByRunimoId(mainRunimoId);
    }

}
