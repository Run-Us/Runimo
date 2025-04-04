package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.service.usecases.query.MainViewQueryUsecase;
import org.runimo.runimo.user.service.dtos.MainViewResponse;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MAIN VIEW")
@RestController
@RequestMapping("/api/v1/main")
@RequiredArgsConstructor
public class MainViewController {

  private final MainViewQueryUsecase mainViewQueryUsecase;


  @Operation(summary = "메인 화면 조회", description = "메인 화면을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메인 화면 조회 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패")
  })
  @GetMapping
  public ResponseEntity<SuccessResponse<MainViewResponse>> queryMainView(
      @UserId Long userId) {
    MainViewResponse response = mainViewQueryUsecase.execute(userId);
    return ResponseEntity.ok(SuccessResponse.of(UserHttpResponseCode.MAIN_PAGE_DATA_FETCHED, response));
  }
}
