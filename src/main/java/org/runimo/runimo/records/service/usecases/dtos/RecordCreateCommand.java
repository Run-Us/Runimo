package org.runimo.runimo.records.service.usecases.dtos;

import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.controller.requests.RecordSaveRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record RecordCreateCommand(
    String userPublicId,
    LocalDateTime startedAt,
    LocalDateTime endAt,
    Pace averagePace,
    Distance totalDistance
) {

  public static RecordCreateCommand from(RecordSaveRequest request) {
    return new RecordCreateCommand(
        request.userPublicId(),
        request.startedAt(),
        request.endAt(),
        new Pace(request.averagePaceInMilliSeconds()),
        new Distance(request.totalDistanceInMeters())
    );
  }

  public Long totalDistanceInMeters() {
    return totalDistance.getAmount();
  }

  public Long totalDurationInSeconds() {
    return ChronoUnit.SECONDS.between(startedAt, endAt);
  }
}
