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
        String objectKey = "uploads/" + UUID.randomUUID() + "_" + fileName;

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))  // The URL expires in 10 minutes.
            .putObjectRequest(createPutObjectRequest(bucketName, objectKey))
            .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        log.info("Presigned URL: [{}]", presignedRequest.url().toString());
        log.debug("HTTP method: [{}]", presignedRequest.httpRequest().method());
        return presignedRequest.url();
    }

    private PutObjectRequest createPutObjectRequest(String bucketName, String objectKey) {
        return PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();
    }
}
