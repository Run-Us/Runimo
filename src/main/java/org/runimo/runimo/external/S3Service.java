package org.runimo.runimo.external;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public URL generatePresignedUrl(String fileName) {
        validateFileName(fileName);
        String objectKey = generateObjectKey(fileName);
        try {
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))  // The URL expires in 10 minutes.
                .putObjectRequest(createPutObjectRequest(bucketName, objectKey))
                .build();
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            log.debug("Presigned URL: [{}]", presignedRequest.url().toString());
            return presignedRequest.url();
        } catch (Exception e) {
            log.error("Error generating presigned URL: {}", e.getMessage());
            throw ExternalServiceException.of(ExternalResponseCode.PRESIGNED_URL_FETCH_FAILED);
        }
    }

    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        validateFileName(file.getOriginalFilename());
        String objectKey = generateObjectKey(file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = createPutObjectRequest(bucketName, objectKey);

            // 파일 데이터로 RequestBody 생성
            RequestBody requestBody = RequestBody.fromInputStream(
                file.getInputStream(),
                file.getSize()
            );

            // S3에 파일 업로드
            PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
            log.debug("파일 업로드 완료: {}, ETag: {}", objectKey, response.eTag());

            // 파일 URL 생성 및 반환
            return getFileUrl(objectKey);
        } catch (IOException e) {
            log.error("파일 업로드 중 I/O 오류 발생: {}", e.getMessage());
            throw ExternalServiceException.of(ExternalResponseCode.FILE_UPLOAD_FAILED);
        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            throw ExternalServiceException.of(ExternalResponseCode.FILE_UPLOAD_FAILED);
        }
    }

    public String getFileUrl(String objectKey) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + objectKey;
    }

    private String generateObjectKey(String fileName) {
        return "uploads/" + UUID.randomUUID() + "_" + sanitizeFileName(fileName);
    }

    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("파일명이 비어있습니다.");
        }
        if (fileName.length() > 255) {
            throw new IllegalArgumentException("파일명이 너무 깁니다. 최대 255자까지 가능합니다.");
        }
    }

    private String sanitizeFileName(String fileName) {
        String sanitized = fileName.replaceAll("\\.\\.[\\\\/]", "");
        sanitized = sanitized.replaceAll("[^a-zA-Z0-9._-]", "_");
        return sanitized;
    }

    private PutObjectRequest createPutObjectRequest(String bucketName, String objectKey) {
        return PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();
    }
}
