package org.runimo.runimo.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.common.CreateUpdateAuditEntity;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.context.annotation.Profile;

@Profile({"prod", "dev"})
@Table(name = "user_refresh_token")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends CreateUpdateAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;
  @Column(name = "refresh_token", nullable = false)
  private String refreshToken;

  @Builder
  private RefreshToken(Long userId, String refreshToken) {
    this.userId = userId;
    this.refreshToken = refreshToken;
  }

  public static RefreshToken of(Long userId, String refreshToken) {
    return RefreshToken.builder()
        .userId(userId)
        .refreshToken(refreshToken)
        .build();
  }

  public void update(String refreshToken) {
    if (refreshToken == null || refreshToken.isEmpty()) {
      throw UserJwtException.of(UserHttpResponseCode.TOKEN_REFRESH_FAIL);
    }
    this.refreshToken = refreshToken;
  }
}
