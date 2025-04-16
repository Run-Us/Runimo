package org.runimo.runimo.user.service.usecases.eggs;

import org.runimo.runimo.user.service.dtos.response.QueryIncubatingEggResponse;

public interface IncubatingEggQueryUsecase {

    QueryIncubatingEggResponse execute(Long userId);
}
