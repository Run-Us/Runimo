package org.runimo.runimo.hatch.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.hatch.service.strategy.EqualRandom;
import org.runimo.runimo.hatch.service.strategy.HatchRandomStrategy;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.item.repository.EggTypeRepository;
import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.runimo.runimo.runimo.repository.RunimoDefinitionRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HatchClient {

    private final RunimoDefinitionRepository runimoDefinitionRepository;
    private final EggTypeRepository eggTypeRepository;

    public RunimoDefinition getRandomRunimoDefinition(EggType eggType) {
        HatchContext hatchContext = new HatchContext();

        // 나올 수 있는 전체 러니모 풀 생성
        List<Long> runimoTypePool = generateRunimoPoolCoveringLowerLevel(eggType);

        // 부화 확률 전략 생성
        HatchRandomStrategy hatchRandomStrategy = new EqualRandom(runimoTypePool.size());

        // 컨택스트에 러니모 풀과 부화 전략 설정
        hatchContext.setHatchStrategy(hatchRandomStrategy);
        hatchContext.setHatchContext(runimoTypePool);

        // 부화
        Long chosenRunimoId = hatchContext.execute();

        return runimoDefinitionRepository.findById(chosenRunimoId)
            .orElseThrow(() -> HatchException.of(
                HatchHttpResponseCode.HATCH_RUNIMO_NOT_FOUND_INTERNAL_ERROR));
    }

    private List<Long> generateRunimoPoolCoveringLowerLevel(EggType eggType) {
        List<EggType> eggTypes = eggTypeRepository.findEggTypeByLevelLessThan(eggType.getLevel());
        return runimoDefinitionRepository.findIdInEggTypes(eggTypes);
    }
}
