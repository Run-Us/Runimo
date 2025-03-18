package org.runimo.runimo.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

// append only entity
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemActivity extends BaseEntity {
  @Column(name = "activity_user_id", nullable = false)
  private Long userId;
  @Column(name = "activity_event_id", nullable = false)
  private Long itemId;

  private Long quantity;
  @Column(name = "activity_event_type", nullable = false)
  private ActivityType type;

  @Builder
  public ItemActivity(Long userId, Long itemId, Long quantity, ActivityType type) {
    this.userId = userId;
    this.itemId = itemId;
    this.quantity = quantity;
    this.type = type;
  }
}
