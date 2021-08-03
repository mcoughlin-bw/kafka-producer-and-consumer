package com.example.kafka.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

@Configuration
public class AwsAuthConfig {

    public static final int ASSUME_ROLE_SESSION_DURATION_SECS = 60 * 60;
    public static final String STS_SESSION_NAME = "call-event-processor";

    @Bean
    public StsClient stsClient(
        @Value("${aws.key.id}") final String accessKeyId,
        @Value("${aws.key.secret}") final String secretKey
    ) {
        final var staticCreds = StaticCredentialsProvider.create(new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return accessKeyId;
            }

            @Override
            public String secretAccessKey() {
                return secretKey;
            }
        });
        return StsClient.builder()
                        .credentialsProvider(staticCreds)
                        .region(Region.US_EAST_1)
                        .build();
    }

    @Bean
    public StsAssumeRoleCredentialsProvider credentialsProvider(
        final StsClient stsClient,
        @Value("${aws.service.role.arn}") final String roleArn) {
        return StsAssumeRoleCredentialsProvider.builder()
                                               .refreshRequest(AssumeRoleRequest.builder()
                                                                                .durationSeconds(ASSUME_ROLE_SESSION_DURATION_SECS)
                                                                                .roleArn(roleArn)
                                                                                .roleSessionName(STS_SESSION_NAME)
                                                                                .build())
                                               .stsClient(stsClient)
                                               .build();
    }
}

