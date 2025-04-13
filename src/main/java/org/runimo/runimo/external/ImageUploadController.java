package org.runimo.runimo.external;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(description = "파일 업로드 관련 API", name = "FILE UPLOAD")
@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class ImageUploadController {

    private final S3Service s3Service;

    @Operation(summary = "Presigned URL 발급", description = "Presigned URL을 발급합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Presigned URL 발급"),
        @ApiResponse(responseCode = "400", description = "데이터 입력 오류"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 오류"),
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<String>> upload(
        @RequestBody ImageUploadRequest request
    ) {
        URL presignedUrl = s3Service.generatePresignedUrl(request.fileName());
        return ResponseEntity.status(201)
            .body(SuccessResponse.of(
                ExternalResponseCode.PRESIGNED_URL_FETCHED,
                presignedUrl.toExternalForm()
            ));
    }

}
