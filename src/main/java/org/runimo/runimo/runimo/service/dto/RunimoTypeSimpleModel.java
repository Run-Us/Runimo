package org.runimo.runimo.runimo.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunimoTypeSimpleModel{

    private Long eggTypeId;
    private String code;
    private String name;
    private String imgUrl;
    private String description;

    public RunimoTypeSimpleModel(Long eggTypeId, String code, String name, String imgUrl,
        String description) {
        this.eggTypeId = eggTypeId;
        this.code = code;
        this.name = name;
        this.imgUrl = imgUrl;
        this.description = description;
    }
}

