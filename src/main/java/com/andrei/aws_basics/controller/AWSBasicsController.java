package com.andrei.aws_basics.controller;

import com.andrei.aws_basics.model.User;
import com.andrei.aws_basics.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("aws-basics")
@RequiredArgsConstructor
public class AWSBasicsController {
    private final S3StorageService service;

    @GetMapping("get-file")
    @ResponseBody
    Flux<User> getFileFromS3Bucket(@RequestParam(name = "fileName") String fileName) {
        return service.getFileFromAsyncS3Bucket(fileName);
    }

    @GetMapping("get-file-bytes")
    @ResponseBody
    Flux<ByteBuffer> getFileBytesFromS3Bucket(@RequestParam(name = "fileName") String fileName) {
        return service.getFileBytesFromAsyncS3Bucket(fileName);
    }

    @PostMapping("upload-file")
    @ResponseBody
    Mono<String> uploadFileToS3Bucket(@RequestBody String line) {
        return service.uploadFileToAsyncS3Bucket(line);
    }
}