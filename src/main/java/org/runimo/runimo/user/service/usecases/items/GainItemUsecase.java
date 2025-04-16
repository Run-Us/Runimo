package org.runimo.runimo.user.service.usecases.items;

import org.runimo.runimo.user.service.dtos.command.GainItemCommand;
import org.runimo.runimo.user.service.dtos.response.GainItemResponse;

public interface GainItemUsecase {

    GainItemResponse gainItem(GainItemCommand command);
}
