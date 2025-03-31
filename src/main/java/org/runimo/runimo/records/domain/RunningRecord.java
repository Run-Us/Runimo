package org.runimo.runimo.records.domain;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.service.usecases.dtos.SegmentPace;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "running_records")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RunningRecord extends BaseEntity {
  private String recordPublicId;
  private Long userId;
  private String title;
  private LocalDateTime startedAt;
  private LocalDateTime endAt;
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
  public RunningRecord(Long userId, String title, LocalDateTime startedAt, LocalDateTime endAt, Distance totalDistance, Pace averagePace, Boolean isRewarded, List<SegmentPace> pacePerKm) {
    this.userId = userId;
    this.title = title;
    this.pacePerKm = pacePerKm;
    this.recordPublicId = UUID.randomUUID().toString();
    this.startedAt = startedAt;
    this.endAt = endAt;
    this.isRewarded = isRewarded;
    this.totalDistance = totalDistance;
    this.averagePace = averagePace;
  }

  public static RunningRecord withoutId(Long userId, String title, LocalDateTime startedAt, LocalDateTime endAt, Distance totalDistance, Pace averagePace) {
    return RunningRecord.builder()
        .userId(userId)
        .title(title)
        .startedAt(startedAt)
        .endAt(endAt)
        .totalDistance(totalDistance)
        .averagePace(averagePace)
        .build();
  }

  public void update(RunningRecord updatedEntity) {
    validateEditor(updatedEntity.userId);
    this.title = updatedEntity.getTitle();
    this.startedAt = updatedEntity.getStartedAt();
    this.endAt = updatedEntity.getEndAt();
    this.averagePace = updatedEntity.averagePace;
    this.recordPublicId = updatedEntity.recordPublicId;
    this.isRewarded = updatedEntity.isRewarded;
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
    return Duration.between(startedAt, endAt);
  }
  private void validateEditor(Long editorId) {
    if (editorId == null || !Objects.equals(this.userId, editorId)) {
      throw new IllegalArgumentException("Invalid editor id");
    }
  }
}
