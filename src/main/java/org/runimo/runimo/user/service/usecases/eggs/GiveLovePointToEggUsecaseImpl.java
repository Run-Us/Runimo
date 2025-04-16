package org.runimo.runimo.user.service.usecases.eggs;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.runimo.runimo.user.service.IncubatingEggProcessor;
import org.runimo.runimo.user.service.LovePointProcessor;
import org.runimo.runimo.user.service.dtos.UseLovePointCommand;
import org.runimo.runimo.user.service.dtos.UseLovePointResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GiveLovePointToEggUsecaseImpl implements GiveLovePointToEggUsecase {

    private final IncubatingEggProcessor incubatingEggProcessor;
    private final LovePointProcessor lovePointProcessor;

    @Override
    @Transactional
    public UseLovePointResponse execute(UseLovePointCommand command) {
        IncubatingEgg incubatingEgg = incubatingEggProcessor.giveLovePoint(command);
        lovePointProcessor.useLovePoint(command.userId(), command.lovePoint());
        return new UseLovePointResponse(
            incubatingEgg.getId(),
            incubatingEgg.getCurrentLovePointAmount(),
            incubatingEgg.getHatchRequireAmount(),
            incubatingEgg.isReadyToHatch()
        );
    }
}
