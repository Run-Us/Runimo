package org.runimo.runimo.user.service.usecases.items;

import org.runimo.runimo.user.service.dtos.UseItemCommand;
import org.runimo.runimo.user.service.dtos.UseItemResponse;

public interface UseItemUsecase {

    UseItemResponse useItem(UseItemCommand command);

}
