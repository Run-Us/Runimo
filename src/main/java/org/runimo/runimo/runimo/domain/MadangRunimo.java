package org.runimo.runimo.runimo.domain;

import lombok.Getter;

@Getter
public enum MadangRunimo implements RunimoType {
  RABBIT("R-101", "토끼"),
  DOG("R-102", "강아지"),
  DUCK("R-103", "오리"),
  WOLF("R-104", "늑대"),
  ;

  private final String code;
  private final String nickname;

  MadangRunimo(String code, String nickname) {
    this.code = code;
    this.nickname = nickname;
  }
}
