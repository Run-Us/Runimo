package org.runimo.runimo.runimo.domain.runimo_type;

import lombok.Getter;

@Getter
public enum GrasslandRunimo implements RunimoType {
    ;

    private final String code;
    private final String nickname;

    GrasslandRunimo(String code, String nickname) {
        this.code = code;
        this.nickname = nickname;
    }
}
