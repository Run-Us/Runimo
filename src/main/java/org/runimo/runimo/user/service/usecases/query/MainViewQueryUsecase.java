package org.runimo.runimo.user.service.usecases.query;

import org.runimo.runimo.user.service.dtos.MainViewResponse;

public interface MainViewQueryUsecase {

    MainViewResponse execute(Long userId);
}
