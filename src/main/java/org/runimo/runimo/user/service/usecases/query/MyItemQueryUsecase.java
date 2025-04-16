package org.runimo.runimo.user.service.usecases.query;

import org.runimo.runimo.user.service.dtos.response.ItemQueryResponse;

public interface MyItemQueryUsecase {

    ItemQueryResponse queryMyEggs(Long userId);

    ItemQueryResponse queryMyAllItems(Long userId);
}
