package org.runimo.runimo.runimo.domain.runimo_type;

import lombok.Getter;

@Getter
public enum ForestRunimo implements RunimoType {
    WOLF_DOG("R-105", "늑대 강아지"),
    FOREST_CAT("R-106", "숲 고양이"),
    LEAF_RABBIT("R-107", "나뭇잎 토끼"),
    FOREST_DUCK("R-108", "숲 오리"),
    ;

    private final String code;
    private final String nickname;

    ForestRunimo(String code, String nickname) {
        this.code = code;
        this.nickname = nickname;
    }
}
