package org.runimo.runimo.user.service.usecases;

import org.runimo.runimo.user.service.dtos.GainItemCommand;
import org.runimo.runimo.user.service.dtos.GainItemResponse;

public interface GainItemUsecase {
  GainItemResponse gainItem(GainItemCommand command);
}
