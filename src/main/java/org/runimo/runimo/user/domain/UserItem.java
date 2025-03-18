package org.runimo.runimo.user.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserItem extends BaseEntity {
  private Long userId;
  private Long itemId;
  private Long quantity;

  public UserItem(Long userId, Long itemId, Long quantity) {
    this.userId = userId;
    this.itemId = itemId;
    this.quantity = quantity;
    validateQuantity(quantity);
  }

  public void useItem(Long quantity) {
    updateQuantity(this.quantity - quantity);
  }

  public void gainItem(Long quantity) {
    updateQuantity(this.quantity + quantity);
  }

  private void updateQuantity(Long quantity) {
    validateQuantity(quantity);
    this.quantity = quantity;
  }


  private void validateQuantity(Long quantity) {
    if(quantity < 0) throw new IllegalArgumentException("quantity must be greater than zero");
  }
}
