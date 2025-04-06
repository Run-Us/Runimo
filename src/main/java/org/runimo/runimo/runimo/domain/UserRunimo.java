package org.runimo.runimo.runimo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Entity
@Table(name = "user_runimo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRunimo extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "runimo_id", nullable = false)
    private Long runimoId;

    @Builder
    private UserRunimo(Long id, Long userId, Long runimoId) {
        this.id = id;
        this.userId = userId;
        this.runimoId = runimoId;
    }
}
