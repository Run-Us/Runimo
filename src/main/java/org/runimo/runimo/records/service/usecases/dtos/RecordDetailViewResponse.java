package org.runimo.runimo.records.service.usecases.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.runimo.runimo.records.domain.RunningRecord;

@Schema(description = "기록 상세 정보")
@Builder
public record RecordDetailViewResponse(
    @Schema(description = "기록 ID", example = "UUID-String")
    String recordId,
    @Schema(description = "기록 제목")
    String title,
    @Schema(description = "기록 시작 시간")
    LocalDateTime startedAt,
    LocalDateTime endAt,
    @Schema(description = "총 기록 시간 (초 단위)")
    Long totalRunningTime,
    @Schema(description = "평균 페이스 (밀리세컨드 단위)")
    Long averagePace,
    @Schema(description = "총 거리 (미터 단위)")
    Long totalDistance,
    @Schema(description = "구간 페이스 리스트")
    List<SegmentPace> segmentPaceList,
    @Schema(description = "기록 이미지 URL")
    String imgUrl
) {

    public static RecordDetailViewResponse from(final RunningRecord runningRecord) {
        return RecordDetailViewResponse.builder()
            .recordId(runningRecord.getRecordPublicId())
            .title(runningRecord.getTitle())
            .startedAt(runningRecord.getStartedAt())
            .endAt(runningRecord.getEndAt())
            .totalRunningTime(runningRecord.getRunningTime().getSeconds())
            .averagePace(runningRecord.getAveragePace().getPaceInMilliSeconds())
            .totalDistance(runningRecord.getTotalDistance().getAmount())
            .segmentPaceList(runningRecord.getPacePerKm())
            .build();
    }
}
