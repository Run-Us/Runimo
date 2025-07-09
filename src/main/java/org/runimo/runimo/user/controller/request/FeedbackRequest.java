package org.runimo.runimo.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.runimo.runimo.user.service.dto.command.FeedbackCommand;

@Schema(description = "피드백 요청 DTO")
public record FeedbackRequest(

    @Schema(description = "평가지표", example = "1")
    @Min(1) @Max(6)
    Integer rate,
    @Schema(description = "피드백 내용", example = "피드백 내용")
    @Length(max = 100)
    String feedback
) {

    public static FeedbackCommand toCommand(Long userId, FeedbackRequest request) {
        return new FeedbackCommand(userId, request.rate(), request.feedback());
    }

}
