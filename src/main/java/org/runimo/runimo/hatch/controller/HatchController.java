package org.runimo.runimo.hatch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.hatch.service.dto.HatchEggResponse;
import org.runimo.runimo.hatch.service.usecase.HatchUsecase;
import org.runimo.runimo.user.controller.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HATCH", description = "알 부화 API")
@RequiredArgsConstructor
@RestController
public class HatchController {

    private final HatchUsecase hatchUsecase;

    @Operation(summary = "알 부화", description = "알을 부화합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "[HSH2011] 알 부화 성공"),
        @ApiResponse(responseCode = "400", description = "[HEH4001] 부화 요청 알이 부화 가능한 상태가 아님"),
        @ApiResponse(responseCode = "404", description = "[HEH4041] 부화 요청 알이 존재하지 않음"),
        @ApiResponse(responseCode = "500", description = "[HEH5001] [서버 내부 오류] 부화될 러니모 존재하지 않음")
    })
    @PostMapping("/api/v1/incubating-eggs/{incubatingEggId}/hatch")
    public ResponseEntity<SuccessResponse<HatchEggResponse>> hatch(
        @UserId Long userId,
        @PathVariable Long incubatingEggId) {
        HatchEggResponse response = hatchUsecase.execute(userId, incubatingEggId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            SuccessResponse.of(
                HatchHttpResponseCode.HATCH_EGG_SUCCESS,
                response)
        );
    }

}
