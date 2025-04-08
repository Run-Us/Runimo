package org.runimo.runimo.auth.jwt;

import org.runimo.runimo.user.domain.SocialProvider;

public record RegisterTokenPayload(
    String providerId,
    SocialProvider socialProvider
) {

}
