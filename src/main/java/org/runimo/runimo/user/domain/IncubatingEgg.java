package org.runimo.runimo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Table(name = "incubating_egg")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncubatingEgg extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "egg_id", nullable = false)
    private Long eggId;

    @Column(name = "current_love_point_amount", nullable = false)
    private Long currentLovePointAmount;

    @Column(name = "hatch_require_amount", nullable = false)
    private Long hatchRequireAmount;

    @Column(name = "egg_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EggStatus status = EggStatus.INCUBATING;

    @Builder
    public IncubatingEgg(Long userId, Long eggId, Long currentLovePointAmount,
        Long hatchRequireAmount, EggStatus status) {
        validateCreation(currentLovePointAmount, hatchRequireAmount);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(eggId);
        Objects.requireNonNull(currentLovePointAmount);
        Objects.requireNonNull(hatchRequireAmount);
        this.userId = userId;
        this.eggId = eggId;
        this.currentLovePointAmount = currentLovePointAmount;
        this.hatchRequireAmount = hatchRequireAmount;
        if(status != null) {
            this.status = status;
        }
    }

    public void gainLovePoint(final Long amount) {
        validateStateTransition(EggStatus.INCUBATING, "부화중인 알에만 애정 포인트 추가가능");
        validateAmount(amount);
        if (currentLovePointAmount + amount > hatchRequireAmount) {
            throw new IllegalArgumentException(
                "애정 포인트가 부화에 필요한 양을 초과했습니다. 현재: " + currentLovePointAmount + ", 추가량: " + amount
                    + ", 필요량: " + hatchRequireAmount);
        }
        this.currentLovePointAmount += amount;
        if (Objects.equals(this.currentLovePointAmount, hatchRequireAmount)) {
            this.status = EggStatus.INCUBATED;
        }
    }

    public void hatch() {
        validateStateTransition(EggStatus.INCUBATED, "애정 포인트가 가득찬 알만 부화 가능");
        this.status = EggStatus.HATCHED;
    }

    private void validateCreation(final Long currentLovePointAmount,
        final Long hatchRequireAmount) {
        if(currentLovePointAmount < 0) {
            throw new IllegalArgumentException("보유한 애정 포인트는 0보다 작을 수 없습니다.");
        }
        if (hatchRequireAmount <= 0) {
            throw new IllegalArgumentException(
                "부화에 필요한 애정 포인트는 0보다 커야 합니다. 현재: " + hatchRequireAmount);
        }
    }

    private void validateStateTransition(final EggStatus expected, final String message) {
        if (status != expected) {
            throw new IllegalStateException(message + " 현재 상태: " + status);
        }
    }

    private void validateAmount(final Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("애정 포인트는 0보다 커야 합니다. 현재: " + amount);
        }
    }

    public boolean isReadyToHatch() {
        return this.status == EggStatus.INCUBATED && currentLovePointAmount.equals(
            hatchRequireAmount);
    }
}
