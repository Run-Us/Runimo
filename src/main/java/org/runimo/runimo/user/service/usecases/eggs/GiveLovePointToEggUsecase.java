package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dtos.command.UseLovePointCommand;
import org.runimo.runimo.user.service.dtos.response.UseLovePointResponse;

public interface GiveLovePointToEggUsecase {

    UseLovePointResponse execute(UseLovePointCommand useLovePointCommand);
}
