package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dtos.RegisterEggResponse;
import org.runimo.runimo.user.service.dtos.RegisterEggCommand;

public interface EggRegisterUsecase {
  RegisterEggResponse execute(RegisterEggCommand command);
}
