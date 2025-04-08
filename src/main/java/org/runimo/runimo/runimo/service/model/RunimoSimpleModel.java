package org.runimo.runimo.runimo.service.model;

import java.util.List;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.runimo.controller.dto.response.RunimoInfo;

public record RunimoSimpleModel(
    Long id,
    String name,
    String imgUrl,
    String code,
    String eggType,
    String description
) {

    public RunimoSimpleModel(Long id, String name, String imgUrl, String code, String eggType,
        String description) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.code = code;
        this.eggType = eggType;
        this.description = description;
    }

    public RunimoSimpleModel(Long id, String name, String imgUrl, String code, EggType eggType,
        String description) {
        this(id, name, imgUrl, code, eggType.name(), description);
    }

    public static List<RunimoInfo> toDtoList(List<RunimoSimpleModel> modelList) {
        return modelList.stream().map(RunimoSimpleModel::toDto).toList();
    }

    private RunimoInfo toDto() {
        return new RunimoInfo(id, name, imgUrl, code, eggType, description);
    }
}
