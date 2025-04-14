package org.runimo.runimo.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class CreatedAuditEntity implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}