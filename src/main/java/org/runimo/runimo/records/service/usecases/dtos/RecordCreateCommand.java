package org.runimo.runimo.records.service.usecases.dtos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.controller.request.RecordSaveRequest;

public record RecordCreateCommand(
    Long userId,
    LocalDateTime startedAt,
    LocalDateTime endAt,
    Long totalTimeInSeconds,
    Pace averagePace,
    Distance totalDistance,
    List<SegmentPace> segmentPaces
) {

    public static RecordCreateCommand from(final RecordSaveRequest request, final Long userId) {
        return new RecordCreateCommand(
            userId,
            request.startedAt(),
            request.endAt(),
            request.totalTimeInSeconds(),
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
