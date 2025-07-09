package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.controller.request.FeedbackRequest;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.usecases.FeedbackUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드백 API")
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackUsecase feedbackUsecase;

    @Operation(summary = "피드백 생성", description = "사용자가 피드백을 작성합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "평가 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
        }
    )
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createFeedback(
        @UserId Long userId,
        @Valid @RequestBody FeedbackRequest request
    ) {
        feedbackUsecase.createFeedback(FeedbackRequest.toCommand(userId, request));
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            SuccessResponse.messageOnly(UserHttpResponseCode.FEEDBACK_CREATED));
    }

}
