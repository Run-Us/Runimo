package org.runimo.runimo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.CreateUpdateAuditEntity;

@Table(name = "user_token")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserDeviceToken extends CreateUpdateAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "device_token", nullable = false)
    private String deviceToken;

    @Column(name = "platform", nullable = false)
    private DevicePlatform platform;

    @Column(name = "notification_allowed", nullable = false)
    private Boolean notificationAllowed;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static UserDeviceToken from(String deviceToken, DevicePlatform platform,
        Boolean notificationAllowed) {
        return UserDeviceToken.builder()
            .deviceToken(deviceToken)
            .notificationAllowed(notificationAllowed)
            .platform(platform)
            .build();
    }

    public void updateNotificationAllowed(Boolean allowed) {
        this.notificationAllowed = allowed;
    }

    public void updateDeviceToken(String deviceToken) {
        validateDeviceToken(deviceToken, this.platform);
        this.deviceToken = deviceToken;
        this.lastUsedAt = LocalDateTime.now();
    }

    private void validateDeviceToken(String deviceToken, DevicePlatform platform) {
        if (deviceToken == null || deviceToken.trim().isEmpty()) {
            throw new IllegalArgumentException("디바이스 토큰은 필수입니다");
        }
    }

}
