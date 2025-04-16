package org.runimo.runimo.user.service.usecases.query;

import org.runimo.runimo.user.service.dtos.response.MainViewResponse;

public interface MainViewQueryUsecase {

    MainViewResponse execute(Long userId);
}
