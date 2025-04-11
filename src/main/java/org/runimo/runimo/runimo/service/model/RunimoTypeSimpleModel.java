package org.runimo.runimo.runimo.service.model;

import java.util.List;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.runimo.controller.dto.response.RunimoTypeInfo;

public record RunimoTypeSimpleModel(
    String name,
    String imgUrl,
    String code,
    String eggType,
    String description
) {

    public RunimoTypeSimpleModel(String name, String imgUrl, String code, EggType eggType,
        String description) {
        this(name, imgUrl, code, eggType.getName(), description);
    }

    public static List<RunimoTypeInfo> toDtoList(List<RunimoTypeSimpleModel> modelList) {
        return modelList.stream().map(RunimoTypeSimpleModel::toDto).toList();
    }

    private RunimoTypeInfo toDto() {
        return new RunimoTypeInfo(name, imgUrl, code, eggType, description);
    }
}
