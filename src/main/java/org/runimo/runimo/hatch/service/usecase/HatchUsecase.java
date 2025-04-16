package org.runimo.runimo.hatch.service.usecase;

import org.runimo.runimo.hatch.controller.dto.response.HatchEggResponse;

public interface HatchUsecase {

    HatchEggResponse execute(Long userId, Long eggId);
}
