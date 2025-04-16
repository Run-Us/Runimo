package org.runimo.runimo.runimo.service.usecase;

import org.runimo.runimo.runimo.service.dto.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.service.dto.response.GetRunimoTypeListResponse;
import org.runimo.runimo.runimo.service.dto.response.SetMainRunimoResponse;

public interface RunimoUsecase {

    GetMyRunimoListResponse getMyRunimoList(Long userId);

    SetMainRunimoResponse setMainRunimo(Long userId, Long runimoId);

    GetRunimoTypeListResponse getRunimoTypeList();
}
