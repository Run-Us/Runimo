package org.runimo.runimo.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

// append only entity
@Table(name = "item_activity")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemActivity extends BaseEntity {
  @Column(name = "activity_user_id", nullable = false)
  private Long userId;
  @Column(name = "activity_item_id", nullable = false)
  private Long itemId;
  @Column(name = "quantity", nullable = false)
  private Long quantity;
  @Column(name = "activity_event_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private ActivityType type;

  @Builder
  public ItemActivity(Long userId, Long itemId, Long quantity, ActivityType type) {
    this.userId = userId;
    this.itemId = itemId;
    this.quantity = quantity;
    this.type = type;
  }
}
