package org.runimo.runimo.external;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public URL generatePresignedUrl(String fileName) {
        validateFileName(fileName);
        String objectKey = "uploads/" + UUID.randomUUID() + "_" + sanitizeFileName(fileName);
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
