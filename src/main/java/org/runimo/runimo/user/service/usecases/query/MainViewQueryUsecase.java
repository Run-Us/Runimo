package org.runimo.runimo.user.service.usecases.query;

import org.runimo.runimo.user.service.dto.response.MainViewResponse;

public interface MainViewQueryUsecase {

    MainViewResponse execute(Long userId);
}
