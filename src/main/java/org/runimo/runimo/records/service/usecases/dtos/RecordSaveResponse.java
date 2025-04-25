package org.runimo.runimo.records.service.usecases.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기록 저장 응답 DTO")
public record RecordSaveResponse(
    @Schema(description = "저장된 기록 ID", example = "UUID-String")
    String savedId
) {

}
