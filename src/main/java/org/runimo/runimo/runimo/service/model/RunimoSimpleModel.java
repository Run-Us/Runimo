package org.runimo.runimo.runimo.service.model;

import java.util.List;
import org.runimo.runimo.runimo.controller.dto.response.RunimoInfo;

public record RunimoSimpleModel(
    Long id,
    String code,
    Long totalRunCount,
    Long totalDistanceInMeters,
    Boolean isMainRunimo
) {

    public RunimoSimpleModel(Long id, String code, Long totalRunCount, Long totalDistanceInMeters,
        Boolean isMainRunimo) {
        this.id = id;
        this.code = code;
        this.totalRunCount = totalRunCount;
        this.totalDistanceInMeters = totalDistanceInMeters;
        this.isMainRunimo = isMainRunimo;
    }

    public static List<RunimoInfo> toDtoList(List<RunimoSimpleModel> modelList) {
        return modelList.stream().map(RunimoSimpleModel::toDto).toList();
    }

    private RunimoInfo toDto() {
        return new RunimoInfo(id, code, totalRunCount, totalDistanceInMeters, isMainRunimo);
    }
}
