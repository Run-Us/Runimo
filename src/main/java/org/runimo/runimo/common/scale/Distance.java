package org.runimo.runimo.common.scale;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Distance implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  private Long amount;

  public Distance(Long amount) {
    this.amount = amount;
  }

  public Long getAmount() {
    return amount;
  }

  public Distance add(Distance distance) {
    return new Distance(this.amount + distance.amount);
  }

  public Distance divide(Distance divisor) {
    return new Distance(this.amount / divisor.getAmount());
  }
}
