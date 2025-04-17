package org.runimo.runimo.runimo.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.exception.RunimoException;
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;
import org.runimo.runimo.runimo.repository.RunimoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunimoService {

    private final RunimoRepository runimoRepository;

    @Transactional
    public void updateRunimoStat(Long runimoId, Long distanceInMeters) {
        Runimo runimo = runimoRepository.findById(runimoId)
            .orElseThrow(() -> RunimoException.of(RunimoHttpResponseCode.RUNIMO_NOT_FOUND));

        runimo.updateStat(distanceInMeters);
    }

}
