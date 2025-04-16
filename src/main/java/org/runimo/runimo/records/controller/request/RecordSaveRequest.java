package org.runimo.runimo.records.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import org.runimo.runimo.records.service.usecases.dtos.SegmentPace;

@Schema(description = "사용자 달리기 기록 저장 요청 DTO")
public record RecordSaveRequest(
    @Schema(description = "달리기 제목", example = "오늘의 달리기")
    LocalDateTime startedAt,
    @Schema(description = "달리기 시작 시각", example = "2021-10-10T10:10:10")
    LocalDateTime endAt,
    @Schema(description = "달린 거리 (미터)", example = "10000")
    Long totalDistanceInMeters,
    @Schema(description = "평균 페이스 (밀리초)", example = "300000")
    Long averagePaceInMilliSeconds,
    @Schema(description = "세그먼트 페이스 리스트")
    List<SegmentPace> segmentPaces
) {

}
