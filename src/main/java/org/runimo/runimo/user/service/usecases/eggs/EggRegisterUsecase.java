package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dto.command.RegisterEggCommand;
import org.runimo.runimo.user.service.dto.response.RegisterEggResponse;

public interface EggRegisterUsecase {

    RegisterEggResponse execute(RegisterEggCommand command);
}
