package org.runimo.runimo.hatch.controller;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.hatch.controller.dto.response.HatchEggResponse;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.hatch.service.usecase.HatchUsecase;
import org.runimo.runimo.user.controller.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HatchController {
    private final HatchUsecase hatchUsecase;

    // TODO : swagger 달기
    @PatchMapping("/api/v1/eggs/{eggId}/hatch")
    public ResponseEntity<SuccessResponse<HatchEggResponse>> hatch(
            @UserId Long userId,
            @PathVariable Long eggId){
        HatchEggResponse response = hatchUsecase.execute(userId, eggId);

        return ResponseEntity.ok().body(
                SuccessResponse.of(
                        HatchHttpResponseCode.HATCH_EGG_SUCCESS,
                        response)
        );
    }

}
