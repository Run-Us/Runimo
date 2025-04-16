package org.runimo.runimo.user.service.usecases.query;

import org.runimo.runimo.user.service.dto.response.MyPageViewResponse;

public interface MyPageQueryUsecase {

    MyPageViewResponse execute(Long userId);
}
