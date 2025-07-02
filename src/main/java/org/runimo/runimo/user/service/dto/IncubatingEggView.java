package org.runimo.runimo.user.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.user.domain.EggStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncubatingEggView {

    private Long id;
    private String name;
    private String imgUrl;
    private Long hatchRequiredPointAmount;
    private Long currentLovePointAmount;
    private Boolean hatchable;
    private String eggCode;

    @Builder
    public IncubatingEggView(Long id, String name, String imgUrl, Long hatchRequiredPointAmount,
        Long currentLovePointAmount, EggStatus hatchable, String eggCode) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.hatchRequiredPointAmount = hatchRequiredPointAmount;
        this.currentLovePointAmount = currentLovePointAmount;
        this.hatchable = (hatchable == EggStatus.INCUBATED);
        this.eggCode = eggCode;
    }
}
