package org.runimo.runimo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Table(name = "apple_user_token")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleUserToken extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "refresh_token")
    private String refreshToken;

    public AppleUserToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
