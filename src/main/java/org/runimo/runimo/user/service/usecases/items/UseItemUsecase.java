package org.runimo.runimo.user.service.usecases.items;

import org.runimo.runimo.user.service.dto.command.UseItemCommand;
import org.runimo.runimo.user.service.dto.response.UseItemResponse;

public interface UseItemUsecase {

    UseItemResponse useItem(UseItemCommand command);

}
