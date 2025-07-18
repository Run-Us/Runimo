package org.runimo.runimo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.runimo.runimo.common.BaseEntity;

@Entity
@Table(name = "users")
@Getter
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE users.id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(name = "public_id", nullable = false)
    private String publicId;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "total_distance_in_meters", nullable = false)
    private Long totalDistanceInMeters = 0L;
    @Column(name = "total_time_in_seconds", nullable = false)
    private Long totalTimeInSeconds = 0L;
    @Column(name = "main_runimo_id")
    private Long mainRunimoId;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;


    @Builder
    public User(String nickname, String imgUrl, Long totalDistanceInMeters,
        Long totalTimeInSeconds, Gender gender) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.totalDistanceInMeters = totalDistanceInMeters != null ? totalDistanceInMeters : 0L;
        this.totalTimeInSeconds = totalTimeInSeconds != null ? totalTimeInSeconds : 0L;
        this.gender = gender;
        this.role = role != null ? role : UserRole.USER;
    }

    public boolean checkUserFirstRun() {
        return totalDistanceInMeters == 0L && totalTimeInSeconds == 0L;
    }

    public void updateRunningStats(Long distance, Long time) {
        this.totalDistanceInMeters += distance;
        this.totalTimeInSeconds += time;
    }

    @PrePersist
    public void prePersist() {
        this.publicId = UUID.randomUUID().toString();
    }

    public void updateMainRunimo(Long runimoId) {
        this.mainRunimoId = runimoId;
    }

    public void withdraw() {
        if (deletedAt != null) {
            throw new IllegalStateException("User is already deleted");
        }
        this.deletedAt = LocalDateTime.now();
    }
}