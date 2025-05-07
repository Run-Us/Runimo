package org.runimo.runimo.common;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class CreateUpdateAuditEntity {

  @CreationTimestamp
  protected LocalDateTime createdAt;

  @UpdateTimestamp
  protected LocalDateTime updatedAt;

}
