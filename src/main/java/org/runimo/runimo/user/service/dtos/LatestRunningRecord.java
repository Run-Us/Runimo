package org.runimo.runimo.user.service.dtos;

import java.time.LocalDateTime;

public record LatestRunningRecord(
    String title,
    LocalDateTime startDateTime,
    Long distanceInMeters,
    Long durationInSeconds,
    Long averagePaceInMiliseconds
) {

}
