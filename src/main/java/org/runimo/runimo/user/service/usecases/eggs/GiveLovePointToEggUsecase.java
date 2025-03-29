package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.controller.UseLovePointResponse;
import org.runimo.runimo.user.service.dtos.UseLovePointCommand;

public interface GiveLovePointToEggUsecase {
  UseLovePointResponse execute(UseLovePointCommand useLovePointCommand);
}
