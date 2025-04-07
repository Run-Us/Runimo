package org.runimo.runimo.hatch.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.runimo.runimo.runimo.repository.RunimoDefinitionRepository;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HatchClient {
    public final RunimoDefinitionRepository runimoDefinitionRepository;

    // TODO : 로직 구현
    public RunimoDefinition getRunimoDefFromEgg(IncubatingEgg incubatingEgg) {
        String dummyRunimoCode = "R-101";

        RunimoDefinition runimoDefinition = runimoDefinitionRepository.findByCode(dummyRunimoCode)
                .orElseThrow(() -> HatchException.of(HatchHttpResponseCode.HATCH_RUNIMO_NOT_FOUND_INTERNAL_ERROR));

        return runimoDefinition;
    }
}
