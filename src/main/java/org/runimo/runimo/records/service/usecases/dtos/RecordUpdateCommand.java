package org.runimo.runimo.records.service.usecases.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record RecordUpdateCommand(
    @NotEmpty Long editorId,
    @NotEmpty String recordPublicId,
    String title,
    String description,
    String imgUrl
) {

}
