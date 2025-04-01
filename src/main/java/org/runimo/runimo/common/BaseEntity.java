package org.runimo.runimo.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @CreationTimestamp
  protected LocalDateTime createdAt;

  @UpdateTimestamp
  protected LocalDateTime updatedAt;

  protected LocalDateTime deletedAt;
}
