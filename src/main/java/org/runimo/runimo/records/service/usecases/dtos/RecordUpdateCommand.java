package org.runimo.runimo.records.service.usecases.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecordUpdateCommand(
    Long editorId,
    String recordPublicId,
    String title,
    LocalDateTime startedAt,
    LocalDateTime endAt,
    Long totalDistanceInMeters,
    Long averagePaceInMilliSeconds
) {
}
