package org.runimo.runimo.user.service.usecases.query;

import org.runimo.runimo.user.service.dtos.ItemQueryResponse;

public interface MyItemQueryUsecase {

    ItemQueryResponse queryMyEggs(Long userId);

    ItemQueryResponse queryMyAllItems(Long userId);
}
