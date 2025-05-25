package org.runimo.runimo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.auth.service.logout.LogOutUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "LOG-OUT", description = "로그아웃 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LogOutController {

    private final LogOutUsecase logOutUsecase;

    @Operation(summary = "로그아웃", description = "사용자를 로그아웃 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "200", description = "로그아웃 성공 (이미 로그아웃된 사용자)"),
        @ApiResponse(responseCode = "401", description = "토큰 검증 실패"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/log-out")
    public ResponseEntity<SuccessResponse<Void>> logOut(
        @RequestHeader("Authorization") String accessTokenHeader
    ) {
        String token = accessTokenHeader.replace("Bearer ", "");
        boolean logoutProcessed = logOutUsecase.execute(token);

        UserHttpResponseCode responseCode = logoutProcessed ? UserHttpResponseCode.LOG_OUT_SUCCESS
            : UserHttpResponseCode.ALREADY_LOG_OUT_SUCCESS;
        return ResponseEntity.ok()
            .body(SuccessResponse.of(responseCode, null));
    }
}
