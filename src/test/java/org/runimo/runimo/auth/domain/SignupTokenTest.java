package org.runimo.runimo.auth.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.runimo.runimo.auth.exceptions.SignUpException;
import org.runimo.runimo.user.domain.SocialProvider;

class SignupTokenTest {

    @Test
    void should_fail_when_token_is_marked() {
        // given
        SignupToken token = SignupToken.builder()
            .token("dummy.token.value")
            .refreshToken("dummy.refresh.token.value")
            .socialProvider(SocialProvider.KAKAO)
            .build();
        token.markAsUsed();

        // when & then
        assertThrows(SignUpException.class, token::markAsUsed);
    }

}