package org.runimo.runimo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(name = "user_withdrawal_reason")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWithdrawReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reason", nullable = false)
    @Enumerated(EnumType.STRING)
    private WithdrawalReason reason;

    @Column(name = "custom_reason")
    private String customReason;

    @Builder
    public UserWithdrawReason(Long id, Long userId, WithdrawalReason reason, String customReason) {
        this.id = id;
        this.userId = userId;
        this.reason = reason;
        this.customReason = customReason;
        validateReason();
    }

    private void validateReason() {
        if (reason != WithdrawalReason.OTHER && (customReason != null && !customReason.isEmpty())) {
            throw new IllegalArgumentException("customReason는 OTHER 타입일 때만 필요합니다.");
        }
    }

}
