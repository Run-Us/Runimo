package org.runimo.runimo.records.service.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordStatDto {
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private Long runningDistanceInMeters;

    public RecordStatDto(LocalDateTime startedAt, LocalDateTime endAt, Long runningDistanceInMeters) {
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.runningDistanceInMeters = runningDistanceInMeters;
    }
}
