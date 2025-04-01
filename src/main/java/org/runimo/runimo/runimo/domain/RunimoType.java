package org.runimo.runimo.runimo.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum RunimoType {
  RABBIT("R-101", "토끼"),
  DOG("R-102", "강아지"),
  DUCK("R-103", "오리"),
  WOLF("R-104", "늑대"),
  ;

  private final String code;
  private final String name;

  RunimoType(String code, String name) {
    this.code = code;
    this.name = name;
  }
}
