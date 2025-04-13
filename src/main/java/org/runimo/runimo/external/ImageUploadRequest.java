package org.runimo.runimo.external;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "이미지 업로드 url발급 요청")
public record ImageUploadRequest(
    @Schema(description = "파일명")
    @NotEmpty String fileName
) {

}
