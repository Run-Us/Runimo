package org.runimo.runimo.records.service.usecases.dtos;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.runimo.runimo.records.domain.RunningRecord;

@Builder
public record RecordDetailViewResponse(
    Long recordId,
    String title,
    LocalDateTime startedAt,
    LocalDateTime endAt,
    Long averagePace,
    Long totalDistance,
    List<SegmentPace> segmentPaceList,
    String imgUrl
) {

    public static RecordDetailViewResponse from(RunningRecord runningRecord) {
        return RecordDetailViewResponse.builder()
            .recordId(runningRecord.getId())
            .title(runningRecord.getTitle())
            .startedAt(runningRecord.getStartedAt())
            .endAt(runningRecord.getEndAt())
            .averagePace(runningRecord.getAveragePace().getPaceInMilliSeconds())
            .totalDistance(runningRecord.getTotalDistance().getAmount())
            .segmentPaceList(runningRecord.getPacePerKm())
            .build();
    }
}
