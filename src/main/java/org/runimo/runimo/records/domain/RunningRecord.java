package org.runimo.runimo.records.domain;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
  private Distance totalDistance;
  @Embedded
  private Pace averagePace;

  @Builder
  public RunningRecord(Long userId, String title, LocalDateTime startedAt, LocalDateTime endAt, Distance totalDistance, Pace averagePace) {
    this.userId = userId;
    this.title = title;
    this.recordPublicId = UUID.randomUUID().toString();
    this.startedAt = startedAt;
    this.endAt = endAt;
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
  }


  private void validateEditor(Long editorId) {
    if (editorId == null || !Objects.equals(this.userId, editorId)) {
      throw new IllegalArgumentException("Invalid editor id");
    }
  }
}
