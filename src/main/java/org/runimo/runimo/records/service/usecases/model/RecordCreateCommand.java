package org.runimo.runimo.records.service.usecases.model;

import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.controller.model.RecordSaveRequest;

import java.time.LocalDateTime;

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
}
