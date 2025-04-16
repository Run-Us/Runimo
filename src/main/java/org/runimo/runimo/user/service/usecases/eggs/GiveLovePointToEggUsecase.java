package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dto.command.UseLovePointCommand;
import org.runimo.runimo.user.service.dto.response.UseLovePointResponse;

public interface GiveLovePointToEggUsecase {

    UseLovePointResponse execute(UseLovePointCommand useLovePointCommand);
}
