package org.runimo.runimo.records.domain;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.service.usecases.dtos.SegmentPace;

@Table(name = "running_record")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RunningRecord extends BaseEntity {

    private String recordPublicId;
    private Long userId;
    private String title;
    private String description;
    private String imgUrl;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private Long totalTimeInSeconds;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_distance"))
    private Distance totalDistance;
    @Embedded
    private Pace averagePace;
    @Column(name = "is_rewarded", nullable = false)
    private Boolean isRewarded;
    @Column(name = "pace_per_km")
    @Convert(converter = SegmentPaceConverter.class)
    private List<SegmentPace> pacePerKm;

    @Builder
    public RunningRecord(
        Long userId,
        String title,
        String description,
        String imgUrl,
        LocalDateTime startedAt,
        LocalDateTime endAt,
        Distance totalDistance,
        Long totalTimeInSeconds,
        Pace averagePace,
        Boolean isRewarded,
        List<SegmentPace> pacePerKm) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.pacePerKm = pacePerKm;
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.isRewarded = isRewarded;
        this.totalDistance = totalDistance;
        this.totalTimeInSeconds = totalTimeInSeconds;
        this.averagePace = averagePace;
        setTitleIfNull();
    }

    public void updateTitle(Long editor, String title) {
        validateEditor(editor);
        this.title = title;
    }

    public void updateDescription(Long editor, String description) {
        validateEditor(editor);
        this.description = description;
    }

    public void updateImageUrl(Long editor, String imgUrl) {
        validateEditor(editor);
        this.imgUrl = imgUrl;
    }

    public void reward(Long editorId) {
        validateEditor(editorId);
        this.isRewarded = true;
    }

    public boolean isRecordAlreadyRewarded() {
        return this.isRewarded;
    }

    public Pace getAveragePace() {
        return new Pace(averagePace.getPaceInMilliSeconds());
    }

    public Distance getTotalDistance() {
        return new Distance(totalDistance.getAmount());
    }

    public Duration getRunningTime() {
        return Duration.of(this.totalTimeInSeconds, ChronoUnit.SECONDS);
    }

    @PrePersist
    public void generateRecordPublicId() {
        if (recordPublicId == null) {
            this.recordPublicId = UUID.randomUUID().toString();
        }
    }

    private void setTitleIfNull() {
        if (this.title != null) {
            return;
        }
        this.title = DefaultTitle.fromTime(this.startedAt).getTitle();
    }

    private void validateEditor(Long editorId) {
        if (editorId == null || !Objects.equals(this.userId, editorId)) {
            throw new IllegalArgumentException("Invalid editor id");
        }
    }
}
