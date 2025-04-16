package org.runimo.runimo.user.service.usecases.items;

import org.runimo.runimo.user.service.dtos.command.UseItemCommand;
import org.runimo.runimo.user.service.dtos.response.UseItemResponse;

public interface UseItemUsecase {

    UseItemResponse useItem(UseItemCommand command);

}
