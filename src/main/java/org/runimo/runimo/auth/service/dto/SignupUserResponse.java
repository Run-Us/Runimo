package org.runimo.runimo.auth.service.dto;

import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.user.domain.User;

public record SignupUserResponse(
    Long userId,
    String nickname,
    String imgUrl,
    TokenPair tokenPair,
    String greetingEggName,
    String greetingEggType,
    String greetingEggImgUrl,
    String eggCode,
    Long eggId
) {

    public SignupUserResponse(final User user, final TokenPair tokenPair, final Egg greetingEgg,
        final String eggCode) {
        this(user.getId(),
            user.getNickname(),
            user.getImgUrl(),
            tokenPair,
            greetingEgg.getName(),
            greetingEgg.getEggType().getName(),
            greetingEgg.getImgUrl(),
            eggCode,
            greetingEgg.getId()
        );
    }
}
