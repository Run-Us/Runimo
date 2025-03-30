package org.runimo.runimo.records.service.usecases.dtos;

import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.controller.requests.RecordSaveRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public record RecordCreateCommand(
    Long userId,
    LocalDateTime startedAt,
    LocalDateTime endAt,
    Pace averagePace,
    Distance totalDistance,
    List<SegmentPace> segmentPaces
) {

  public static RecordCreateCommand from(final RecordSaveRequest request, final Long userId) {
    return new RecordCreateCommand(
        userId,
        request.startedAt(),
        request.endAt(),
        new Pace(request.averagePaceInMilliSeconds()),
        new Distance(request.totalDistanceInMeters()),
        request.segmentPaces()
    );
  }

  public Long totalDistanceInMeters() {
    return totalDistance.getAmount();
  }

  public Long totalDurationInSeconds() {
    return ChronoUnit.SECONDS.between(startedAt, endAt);
  }
}
