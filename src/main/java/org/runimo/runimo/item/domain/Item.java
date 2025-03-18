package org.runimo.runimo.item.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.runimo.runimo.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

  @NaturalId
  private String itemCode;

  private String name;

  private String description;

  private String imgUrl;

  @Enumerated(EnumType.STRING)
  private ItemType itemType;

  @Builder
  public Item(String itemCode, String name, String description, String imgUrl, ItemType itemType) {
    this.itemCode = itemCode;
    this.name = name;
    this.description = description;
    this.imgUrl = imgUrl;
    this.itemType = itemType;
  }
}
