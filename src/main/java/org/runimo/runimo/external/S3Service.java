package org.runimo.runimo.external;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public URL generatePresignedUrl(String fileName) {
        String objectKey = "uploads/" + UUID.randomUUID() + "_" + fileName;

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
            .getObjectRequest(createGetObjectRequest(bucketName, objectKey))
            .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        log.info("Presigned URL: [{}]", presignedRequest.url().toString());
        log.debug("HTTP method: [{}]", presignedRequest.httpRequest().method());
        return presignedRequest.url();

    }

    private GetObjectRequest createGetObjectRequest(String bucketName, String objectKey) {
        return GetObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();
    }
}
