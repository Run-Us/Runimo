package org.runimo.runimo.runimo.service.usecase;

import org.runimo.runimo.runimo.service.dtos.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.service.dtos.response.GetRunimoTypeListResponse;
import org.runimo.runimo.runimo.service.dtos.response.SetMainRunimoResponse;

public interface RunimoUsecase {

    GetMyRunimoListResponse getMyRunimoList(Long userId);

    SetMainRunimoResponse setMainRunimo(Long userId, Long runimoId);

    GetRunimoTypeListResponse getRunimoTypeList();
}
