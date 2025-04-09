package org.runimo.runimo.hatch.service.usecase;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.hatch.controller.dto.response.HatchEggResponse;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.hatch.service.HatchClient;
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
    private final HatchClient hatchClient;

    @Transactional
    @Override
    public HatchEggResponse execute(Long userId, Long incubatingEggId) {
        IncubatingEgg incubatingEgg = incubatingEggRepository.findById(incubatingEggId)
            .orElseThrow(() -> HatchException.of(HatchHttpResponseCode.HATCH_EGG_NOT_FOUND));
        validAndHatchIncubatingEgg(incubatingEgg);

        // 부화 - 러니모 획득
        RunimoDefinition runimoDefinition = hatchClient.getRunimoDefFromEgg(incubatingEgg);
        // 이미 보유한 러니모인지 확인
        boolean isDuplicatedRunimo = runimoRepository.existsByUserIdAndRunimoDefinitionId(userId,
            runimoDefinition.getId());

        // 러니모 저장 (중복 아닐 경우만)
        if (!isDuplicatedRunimo) {
            Runimo runimo = Runimo.builder()
                .userId(userId)
                .runimoDefinitionId(runimoDefinition.getId())
                .build();
            runimoRepository.save(runimo);
        }

        return new HatchEggResponse(
            runimoDefinition.getName(),
            runimoDefinition.getImgUrl(),
            runimoDefinition.getCode(),
            isDuplicatedRunimo
        );
    }

    void validAndHatchIncubatingEgg(IncubatingEgg incubatingEgg) {
        if (incubatingEgg.isReadyToHatch()) {
            incubatingEgg.hatch();
        } else {
            throw HatchException.of(HatchHttpResponseCode.HATCH_EGG_NOT_READY);
        }
    }
}
