package org.runimo.runimo.user.service.usecases;

import org.runimo.runimo.user.service.dtos.ItemQueryResponse;

public interface MyItemQueryUsecase {
  ItemQueryResponse execute(Long userId);
}
