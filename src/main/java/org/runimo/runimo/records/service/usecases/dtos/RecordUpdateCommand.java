package org.runimo.runimo.records.service.usecases.dtos;

import java.time.LocalDateTime;
import lombok.Builder;

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
