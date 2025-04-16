package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dtos.command.RegisterEggCommand;
import org.runimo.runimo.user.service.dtos.response.RegisterEggResponse;

public interface EggRegisterUsecase {

    RegisterEggResponse execute(RegisterEggCommand command);
}
