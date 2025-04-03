package org.runimo.runimo.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.runimo.runimo.common.BaseEntity;

@Table(name = "item")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
public abstract class Item extends BaseEntity {

  @Column(name = "item_code", nullable = false, unique = true)
  @NaturalId
  private String itemCode;

  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "description")
  private String description;

  @Column(name = "img_url")
  private String imgUrl;

  @Column(name = "item_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private ItemType itemType;

  protected Item(String itemCode, String name, String description, String imgUrl, ItemType itemType) {
    this.itemCode = itemCode;
    this.name = name;
    this.description = description;
    this.imgUrl = imgUrl;
    this.itemType = itemType;
  }
}
