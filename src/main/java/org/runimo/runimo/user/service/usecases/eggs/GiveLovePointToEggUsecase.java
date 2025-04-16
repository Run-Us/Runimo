package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dtos.UseLovePointCommand;
import org.runimo.runimo.user.service.dtos.UseLovePointResponse;

public interface GiveLovePointToEggUsecase {

    UseLovePointResponse execute(UseLovePointCommand useLovePointCommand);
}
