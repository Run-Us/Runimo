package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dtos.RegisterEggCommand;
import org.runimo.runimo.user.service.dtos.RegisterEggResponse;

public interface EggRegisterUsecase {

    RegisterEggResponse execute(RegisterEggCommand command);
}
