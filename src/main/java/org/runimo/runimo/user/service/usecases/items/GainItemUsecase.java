package org.runimo.runimo.user.service.usecases.items;

import org.runimo.runimo.user.service.dto.command.GainItemCommand;
import org.runimo.runimo.user.service.dto.response.GainItemResponse;

public interface GainItemUsecase {

    GainItemResponse gainItem(GainItemCommand command);
}
