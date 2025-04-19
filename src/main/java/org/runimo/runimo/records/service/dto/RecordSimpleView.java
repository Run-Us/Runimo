package org.runimo.runimo.records.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;

@Schema(description = "기록 요약 정보")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordSimpleView {
    @Schema(description = "기록 ID", example = "UUID-String")
    private String id;
    private String title;
    private LocalDateTime startDateTime;
    private Long distanceInMeters;
    private Long durationInSeconds;
    private Long averagePaceInMiliseconds;

    @Builder
    private RecordSimpleView(String id, String title, LocalDateTime startDateTime,
        Long distanceInMeters,
        Long durationInSeconds, Long averagePaceInMiliseconds) {
        this.id = id;
        this.title = title;
        this.startDateTime = startDateTime;
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
        this.averagePaceInMiliseconds = averagePaceInMiliseconds;
    }

    public static RecordSimpleView from(RunningRecord runningRecord) {
        return RecordSimpleView.builder()
            .id(runningRecord.getRecordPublicId())
            .title(runningRecord.getTitle())
            .startDateTime(runningRecord.getStartedAt())
            .distanceInMeters(runningRecord.getTotalDistance().getAmount())
            .durationInSeconds(runningRecord.getRunningTime().getSeconds())
            .averagePaceInMiliseconds(runningRecord.getAveragePace().getPaceInMilliSeconds())
            .build();
    }
}
