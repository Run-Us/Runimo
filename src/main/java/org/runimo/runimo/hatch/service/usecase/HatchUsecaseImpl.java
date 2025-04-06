package org.runimo.runimo.hatch.service.usecase;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.hatch.controller.dto.response.HatchEggResponse;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.hatch.service.HatchClient;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.domain.UserRunimo;
import org.runimo.runimo.runimo.repository.RunimoRepository;
import org.runimo.runimo.runimo.repository.UserRunimoRepository;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.runimo.runimo.user.repository.IncubatingEggRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HatchUsecaseImpl implements HatchUsecase {
    private final RunimoRepository runimoRepository;
    private final IncubatingEggRepository incubatingEggRepository;
    private final UserRunimoRepository userRunimoRepository;
    private final HatchClient hatchClient;

    @Transactional
    @Override
    public HatchEggResponse execute(Long userId, Long incubatingEggId) {
        IncubatingEgg incubatingEgg = incubatingEggRepository.findById(incubatingEggId)
                .orElseThrow(() -> HatchException.of(HatchHttpResponseCode.HATCH_EGG_NOT_FOUND));
        validAndHatchIncubatingEgg(incubatingEgg);

        // 부화 - 러니모 획득
        Runimo runimo = hatchClient.getRunimoFromEgg(incubatingEgg);

        // 이미 보유한 러니모인지 확인
        boolean isDuplicatedRunimo = userRunimoRepository.existsByUserIdAndRunimoId(userId, runimo.getId());

        // 러니모 저장 -> 나중에 다른 요청으로 빼야 할 듯
        Runimo runimoSaved = runimoRepository.save(runimo);
        UserRunimo userRunimo = UserRunimo.builder()
                .userId(userId)
                .runimoId(runimoSaved.getId())
                .build();
        userRunimoRepository.save(userRunimo);

        return new HatchEggResponse(
                runimo.getName(),
                runimo.getImgUrl(),
                runimo.getCode(),
                isDuplicatedRunimo
        );
    }

    void validAndHatchIncubatingEgg(IncubatingEgg incubatingEgg){
        if(incubatingEgg.isReadyToHatch()){
            incubatingEgg.hatch();
        } else {
            throw HatchException.of(HatchHttpResponseCode.HATCH_EGG_NOT_READY);
        }
    }
}
