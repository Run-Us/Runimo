package org.runimo.runimo.runimo.service.dtos;

import java.util.List;

public record RunimoSimpleModel(
    Long id,
    String code,
    Long totalRunCount,
    Long totalDistanceInMeters
) {

    public static List<RunimoInfo> toDtoList(List<RunimoSimpleModel> modelList, Long mainRunimoId) {
        return modelList.stream().map(i ->
            i.toDto(i.id.equals(mainRunimoId))
        ).toList();
    }

    private RunimoInfo toDto(Boolean isMainRunimo) {
        return new RunimoInfo(id, code, totalRunCount, totalDistanceInMeters, isMainRunimo);
    }
}
