package org.runimo.runimo.auth.jwt;

import org.runimo.runimo.user.domain.SocialProvider;

public record SignupTokenPayload(
    String token,
    String providerId,
    SocialProvider socialProvider
) {

}
