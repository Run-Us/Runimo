package org.runimo.runimo.user.service.usecases.eggs;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.service.ItemFinder;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.runimo.runimo.user.service.IncubatingEggProcessor;
import org.runimo.runimo.user.service.UserItemProcessor;
import org.runimo.runimo.user.service.dtos.command.RegisterEggCommand;
import org.runimo.runimo.user.service.dtos.response.RegisterEggResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EggRegisterUsecaseImpl implements EggRegisterUsecase {

    private final ItemFinder itemFinder;
    private final IncubatingEggProcessor incubatingEggProcessor;
    private final UserItemProcessor userItemProcessor;

    @Override
    public RegisterEggResponse execute(RegisterEggCommand command) {
        Egg egg = (Egg) itemFinder.findById(command.itemId())
            .orElseThrow(() -> new IllegalArgumentException("Egg not found"));
        userItemProcessor.useItem(command.userId(), command.itemId(), 1L);
        IncubatingEgg incubatingEgg = incubatingEggProcessor.create(command.userId(), egg);
        return new RegisterEggResponse(
            incubatingEgg.getId(),
            incubatingEgg.getCurrentLovePointAmount(),
            incubatingEgg.getHatchRequireAmount()
        );
    }
}
