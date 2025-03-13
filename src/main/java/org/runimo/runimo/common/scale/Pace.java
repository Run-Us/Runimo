package org.runimo.runimo.common.scale;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pace implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  private Long paceInMilliSeconds;

  public Pace(Long paceInMilliSeconds) {
    this.paceInMilliSeconds = paceInMilliSeconds;
  }
}
