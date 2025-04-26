package org.runimo.runimo.item.domain;

import static org.runimo.runimo.common.GlobalConsts.EMPTYFIELD;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("EGG")
public class Egg extends Item {

    public static final Egg EMPTY = new Egg(EMPTYFIELD, EMPTYFIELD, EMPTYFIELD, EMPTYFIELD,
        EggType.EMPTY, 0L);
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "egg_type_id", referencedColumnName = "id")
    private EggType eggType;
    @Column(name = "hatch_require_amount")
    private Long hatchRequireAmount;

    @Builder
    public Egg(String itemCode, String name, String description, String imgUrl, EggType eggType,
        Long hatchRequireAmount) {
        super(itemCode, name, description, imgUrl, ItemType.USABLE);
        this.eggType = eggType;
        this.hatchRequireAmount = hatchRequireAmount;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}