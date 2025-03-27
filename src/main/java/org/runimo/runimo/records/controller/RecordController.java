package org.runimo.runimo.records.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.records.controller.requests.RecordSaveRequest;
import org.runimo.runimo.records.controller.requests.RecordUpdateRequest;
import org.runimo.runimo.records.enums.RecordHttpResponse;
import org.runimo.runimo.records.service.usecases.RecordCreateUsecase;
import org.runimo.runimo.records.service.usecases.RecordQueryUsecase;
import org.runimo.runimo.records.service.usecases.RecordUpdateUsecase;
import org.runimo.runimo.records.service.usecases.dtos.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;
import org.runimo.runimo.records.service.usecases.dtos.RecordSaveResponse;
import org.runimo.runimo.user.controller.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "RECORD", description = "기록 관련 API")
@RequestMapping("/api/v1/records")
@RestController
@RequiredArgsConstructor
public class RecordController {

  private final RecordCreateUsecase recordCreateUsecase;
  private final RecordUpdateUsecase recordUpdateUsecase;
  private final RecordQueryUsecase recordQueryUsecase;

  @Operation(summary = "기록 저장", description = "기록을 저장합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "기록 저장 성공",
          content = @Content(schema = @Schema(implementation = RecordSaveResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패")
  })
  @PostMapping
  public ResponseEntity<SuccessResponse<RecordSaveResponse>> saveRecord(
      @RequestBody RecordSaveRequest request,
      @UserId Long userId
  ) {
    RecordSaveResponse response = recordCreateUsecase.execute(RecordCreateCommand.from(request, userId));
    return ResponseEntity.created(URI.create("/api/v1/records/" + response.savedId()))
        .body(SuccessResponse.of(RecordHttpResponse.RECORD_SAVED, response));
  }

  @Operation(summary = "기록 조회", description = "기록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "기록 조회 성공",
          content = @Content(schema = @Schema(implementation = RecordDetailViewResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패")
  })
  @GetMapping("/{recordId}")
  public ResponseEntity<RecordDetailViewResponse> viewRecord(
      @PathVariable String recordId
  ) {
    RecordDetailViewResponse response = recordQueryUsecase.getRecordDetailView(recordId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "기록 수정", description = "기록을 수정합니다.")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "기록 수정 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
          @ApiResponse(responseCode = "401", description = "인증 실패")
      }
  )
  @PatchMapping("/{recordId}")
  public ResponseEntity<Void> updateRecord(
      @RequestBody RecordUpdateRequest request,
      @UserId Long userId
  ) {
    recordUpdateUsecase.updateRecord(RecordUpdateRequest.toCommand(userId, request));
    return ResponseEntity.ok().build();
  }
}
