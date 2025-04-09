package org.runimo.runimo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Table(name = "user_love_point")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LovePoint extends BaseEntity {

    public static final LovePoint EMPTY = new LovePoint(null, 0L);
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Builder
    public LovePoint(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public void add(long amount) {
        this.amount += amount;
    }

    public void subtract(long amount) {
        if (this.amount < amount) {
            throw new IllegalStateException("Not enough love points");
        }
        this.amount -= amount;
    }
}
