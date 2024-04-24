package com.andrei.aws_basics.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class StorageConfig {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String accessSecret;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region).build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, accessSecret);
        return S3AsyncClient.builder().credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.EU_NORTH_1).build();
    }
}