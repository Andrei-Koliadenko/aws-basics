package com.andrei.aws_basics.service;

import com.andrei.aws_basics.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3StorageService {
    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);
    @Value("${application.bucket.name}")
    private String bucketName;
    private final S3AsyncClient s3AsyncClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String FILE_NAME = "test1.txt";

    public Flux<ByteBuffer> getFileBytesFromAsyncS3Bucket(String filename) {
        return Mono.fromFuture(s3AsyncClient.getObject(req ->
                                req.bucket(bucketName).key(filename),
                        AsyncResponseTransformer.toPublisher()))
                .flatMapMany(Flux::from);
    }

    public Flux<User> getFileFromAsyncS3Bucket(String filename) {
        return Mono.fromFuture(s3AsyncClient.getObject(req ->
                                req.bucket(bucketName).key(filename),
                        AsyncResponseTransformer.toBlockingInputStream()))
                .flatMapIterable(this::mapUsersFromStream);
    }

    private List<User> mapUsersFromStream(ResponseInputStream<GetObjectResponse> stream) {
        try {
            return objectMapper.readValue(stream, new TypeReference<>() {
            });
        } catch (IOException ignored) {
        }
        return List.of();
    }

    public Mono<String> uploadFileToAsyncS3Bucket(String line) {
        return Mono.fromFuture(s3AsyncClient.putObject(PutObjectRequest
                                .builder()
                                .bucket(bucketName)
                                .key(FILE_NAME)
                                .build(),
                        AsyncRequestBody.fromString(line)))
                .map(PutObjectResponse::eTag);
    }
}
