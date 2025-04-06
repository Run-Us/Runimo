package org.runimo.runimo.runimo.service.usecase;

import org.runimo.runimo.runimo.controller.dto.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.controller.dto.response.SetMainRunimoResponse;

public interface RunimoUsecase {
    GetMyRunimoListResponse getMyRunimoList(Long userId);

    SetMainRunimoResponse setMainRunimo(Long userId, Long runimoId);
}
