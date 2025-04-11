package org.runimo.runimo.runimo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.runimo.controller.dto.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.controller.dto.response.GetRunimoTypeListResponse;
import org.runimo.runimo.runimo.controller.dto.response.SetMainRunimoResponse;
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;
import org.runimo.runimo.runimo.service.usecase.RunimoUsecase;
import org.runimo.runimo.user.controller.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RUNIMO", description = "러니모 관련 API")
@RequiredArgsConstructor
@RestController
public class RunimoController {

    private final RunimoUsecase runimoUsecase;

    @Operation(summary = "보유 러니모 조회", description = "사용자가 보유한 러니모 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "[MSH2001] 나의 보유 러니모 목록 조회 성공")
    })
    @GetMapping("/api/v1/users/me/runimos")
    public ResponseEntity<SuccessResponse<GetMyRunimoListResponse>> getMyRunimoList(
        @UserId Long userId) {
        GetMyRunimoListResponse response = runimoUsecase.getMyRunimoList(userId);

        return ResponseEntity.ok().body(
            SuccessResponse.of(
                RunimoHttpResponseCode.GET_MY_RUNIMO_LIST_SUCCESS,
                response)
        );
    }

    @Operation(summary = "전체 러니모 종류 조회", description = "전체 러니모 종류를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "[MSH2003] 전체 러니모 종류 조회 성공")
    })
    @GetMapping("/api/v1/runimos/types/all")
    public ResponseEntity<SuccessResponse<GetRunimoTypeListResponse>> getRunimoTypeList() {
        GetRunimoTypeListResponse response = runimoUsecase.getRunimoTypeList();

        return ResponseEntity.ok().body(
            SuccessResponse.of(
                RunimoHttpResponseCode.GET_ALL_RUNIMO_TYPE_LIST_SUCCESS,
                response)
        );
    }

    @Operation(summary = "대표 러니모 설정", description = "사용자의 대표 러니모를 설정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "[MSH2002] 대표 러니모 설정 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "[MSH4031] 요청 러니모의 소유자가 아님"),
        @ApiResponse(responseCode = "404", description = "[MSH4041] 요청 러니모가 존재하지 않음")
    })
    @PatchMapping("/api/v1/runimos/{runimoId}/main")
    public ResponseEntity<SuccessResponse<SetMainRunimoResponse>> setMainRunimo(
        @UserId Long userId,
        @PathVariable Long runimoId) {
        SetMainRunimoResponse response = runimoUsecase.setMainRunimo(userId, runimoId);

        return ResponseEntity.ok().body(
            SuccessResponse.of(
                RunimoHttpResponseCode.SET_MAIN_RUNIMO_SUCCESS,
                response)
        );
    }

}
