package org.runimo.runimo.runimo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_runimos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRunimo {
    @Id
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "runimo_id", nullable = false)
    private Long runimoId;

    @Builder
    public UserRunimo(Long id, Long userId, Long runimoId) {
        this.id = id;
        this.userId = userId;
        this.runimoId = runimoId;
    }
}
