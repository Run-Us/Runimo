package org.runimo.runimo.hatch.service.usecase;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.hatch.service.HatchClient;
import org.runimo.runimo.hatch.service.dto.HatchEggResponse;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.service.ItemFinder;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.runimo.runimo.runimo.repository.RunimoRepository;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.runimo.runimo.user.repository.IncubatingEggRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HatchUsecaseImpl implements HatchUsecase {

    private final IncubatingEggRepository incubatingEggRepository;
    private final RunimoRepository runimoRepository;
    private final ItemFinder itemFinder;
    private final HatchClient hatchClient;

    @Transactional
    @Override
    public HatchEggResponse execute(Long userId, Long incubatingEggId) {
        IncubatingEgg incubatingEgg = getIncubatingEgg(incubatingEggId);
        validAndHatchIncubatingEgg(incubatingEgg);

        Egg egg = getEgg(incubatingEgg.getEggId());

        // 부화 - 러니모 획득
        RunimoDefinition runimoDefinition = hatchClient.getRandomRunimoDefinition(egg.getEggType());

        // 이미 보유한 러니모인지 확인
        boolean isDuplicatedRunimo = runimoRepository.existsByUserIdAndRunimoDefinitionId(userId,
            runimoDefinition.getId());

        // 러니모 저장 (중복 아닐 경우만)
        Long runimoId = null;
        if (!isDuplicatedRunimo) {
            Runimo runimo = Runimo.builder()
                .userId(userId)
                .runimoDefinitionId(runimoDefinition.getId())
                .build();
            runimoId = runimoRepository.save(runimo).getId();
        }

        return new HatchEggResponse(
            runimoId,
            runimoDefinition.getName(),
            runimoDefinition.getImgUrl(),
            runimoDefinition.getCode(),
            isDuplicatedRunimo,
            egg.getEggType().getCode()
        );
    }

    private Egg getEgg(Long eggId) {
        return (Egg) itemFinder.findById(eggId).orElseThrow(
            () -> HatchException.of(HatchHttpResponseCode.HATCH_EGG_TYPE_NOT_FOUND_INTERNAL_ERROR));
    }

    private IncubatingEgg getIncubatingEgg(Long incubatingEggId) {
        return incubatingEggRepository.findById(incubatingEggId)
            .orElseThrow(() -> HatchException.of(HatchHttpResponseCode.HATCH_EGG_NOT_FOUND));
    }

    void validAndHatchIncubatingEgg(IncubatingEgg incubatingEgg) {
        if (incubatingEgg.isReadyToHatch()) {
            incubatingEgg.hatch();
        } else {
            throw HatchException.of(HatchHttpResponseCode.HATCH_EGG_NOT_READY);
        }
    }
}
