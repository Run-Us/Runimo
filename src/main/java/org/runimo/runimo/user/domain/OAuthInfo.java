package org.runimo.runimo.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.BaseEntity;

@Entity
@Table(name = "oauth_accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthInfo extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "provider", nullable = false)
  private SocialProvider provider;

  @Column(name = "provider_id", nullable = false)
  private String providerId;

  /***
   * OAuthInfo Constructor
   * @param user OAuth연동할 유저 엔티티
   * @param provider 소셜 로그인 제공자
   * @param providerId 소셜 로그인 이메일 / guid
   */
  @Builder
  public OAuthInfo(
      @NotNull User user, @NotNull SocialProvider provider, @NotNull String providerId) {
    this.user = user;
    this.provider = provider;
    this.providerId = providerId;
  }
}
