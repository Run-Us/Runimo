package org.runimo.runimo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Presigner amazonS3Client() {
        return S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(
                () -> software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(accessKey, secretKey)
            )
            .build();
    }
}
