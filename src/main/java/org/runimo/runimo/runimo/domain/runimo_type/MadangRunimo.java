package org.runimo.runimo.runimo.domain.runimo_type;

import lombok.Getter;

@Getter
public enum MadangRunimo implements RunimoType {
    DOG("R-101", "강아지"),
    CAT("R-102", "고양이"),
    RABBIT("R-103", "토끼"),
    DUCK("R-104", "오리"),
    ;

    private final String code;
    private final String nickname;

    MadangRunimo(String code, String nickname) {
        this.code = code;
        this.nickname = nickname;
    }
}
