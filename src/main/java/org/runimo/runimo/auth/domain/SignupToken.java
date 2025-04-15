package org.runimo.runimo.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.CreatedAuditEntity;
import org.runimo.runimo.user.domain.SocialProvider;

@Table(name = "signup_token")
@Entity
@Getter
@NoArgsConstructor
public class SignupToken extends CreatedAuditEntity {

    @Id
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

    @Builder
    public SignupToken(String token, String providerId, String refreshToken,
        SocialProvider socialProvider) {
        this.token = token;
        this.providerId = providerId;
        this.refreshToken = refreshToken;
        this.socialProvider = socialProvider;
    }
}
