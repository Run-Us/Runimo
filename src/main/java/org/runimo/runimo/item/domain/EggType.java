package org.runimo.runimo.item.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum EggType {
  MADANG("A100", "마당", 0L),
  FOREST("A101", "숲", 30000L),
  GRASSLAND("A102", "초원", 50000L);

  private final String code;
  private final String name;
  private final Long requiredDistanceInMeters;


  EggType(String code, String name, Long requiredDistanceInMeters) {
    this.code = code;
    this.name = name;
    this.requiredDistanceInMeters = requiredDistanceInMeters;
  }

  public static List<EggType> getUnLockedEggTypes(final Long distance) {
    return Arrays.stream(EggType.values())
        .filter(type -> type.requiredDistanceInMeters < distance)
        .toList();
  }
}
