package org.runimo.runimo.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WithdrawalReason {
    NOT_ENOUGH_CHARACTERS("수집할 캐릭터가 부족해요"),
    NO_LONGER_NEEDED("러닝 목표에 도움이 되지 않아요"),
    RECORD_NOT_EXPECTED("기록 기능이 기대와 달라요"),
    COMPLEX_USAGE("앱 사용이 복잡해요"),
    USING_OTHER_SERVICE("다른 러닝 앱을 사용하고 있어요"),
    PRIVACY_CONCERN("개인정보가 걱정돼요"),
    OTHER("기타 (직접 입력)");

    private final String description;
}
