package org.runimo.runimo.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.runimo.runimo.common.GlobalConsts.EMPTYFIELD;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Egg extends Item {
  public static final Egg EMPTY = new Egg(EMPTYFIELD, EMPTYFIELD, EMPTYFIELD, EMPTYFIELD, null, 0L);
  @Column(name = "egg_type")
  @Enumerated(EnumType.STRING)
  private EggType eggType;
  @Column(name = "hatch_require_amount")
  private Long hatchRequireAmount;

  @Builder
  public Egg(String itemCode, String name, String description, String imgUrl, EggType eggType, Long hatchRequireAmount) {
    super(itemCode, name, description, imgUrl, ItemType.USABLE);
    this.eggType = eggType;
    this.hatchRequireAmount = hatchRequireAmount;
  }
}