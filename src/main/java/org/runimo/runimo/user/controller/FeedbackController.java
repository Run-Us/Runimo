package org.runimo.runimo.user.controller;

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

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackUsecase feedbackUsecase;

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
