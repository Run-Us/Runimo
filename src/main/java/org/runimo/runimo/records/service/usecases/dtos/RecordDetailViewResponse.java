package org.runimo.runimo.records.service.usecases.dtos;

import lombok.Builder;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.domain.RunningRecord;

import java.time.LocalDateTime;

@Builder
public record RecordDetailViewResponse(
    String title,
    LocalDateTime startedAt,
    LocalDateTime endAt,
    Pace averagePace,
    Distance totalDistance,
    String imgUrl
) {
  public static RecordDetailViewResponse from(RunningRecord runningRecord) {
    return RecordDetailViewResponse.builder()
        .title(runningRecord.getTitle())
        .startedAt(runningRecord.getStartedAt())
        .endAt(runningRecord.getEndAt())
        .averagePace(runningRecord.getAveragePace())
        .totalDistance(runningRecord.getTotalDistance())
        .build();
  }
}
