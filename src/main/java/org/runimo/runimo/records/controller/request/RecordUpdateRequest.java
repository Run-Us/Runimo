package org.runimo.runimo.records.controller.request;


import io.swagger.v3.oas.annotations.media.Schema;
import org.runimo.runimo.records.service.usecases.dtos.RecordUpdateCommand;

@Schema(description = "사용자 달리기 기록 수정 요청 DTO")
public record RecordUpdateRequest(
    @Schema(description = "달리기 제목", example = "오늘의 달리기")
    String title,
    @Schema(description = "달리기 설명", example = "오늘은 올림픽 공원을 달렸어요")
    String description,
    @Schema(description = "대표 이미지 URL", example = "https://example.com/image.jpg")
    String imgUrl
) {

    public static RecordUpdateCommand toCommand(Long userId, String recordPublicId, RecordUpdateRequest request) {
        return RecordUpdateCommand.builder()
            .recordPublicId(recordPublicId)
            .editorId(userId)
            .title(request.title)
            .description(request.description)
            .imgUrl(request.imgUrl)
            .build();
    }
}
