package com.skillbridgebackend.codereviewservice.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class UploadController {

    @Value("${app.s3.bucket}") String bucket;
    @Value("${app.aws.region:ap-south-1}") String region;

    @PostMapping("/upload-url")
    public Map<String, Object> createUploadUrl(@RequestParam String menteeId) {
        String key = "uploads/" + menteeId + "/" + System.currentTimeMillis() + ".zip";

        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType("application/zip")
                    .build();

            PresignPutObjectRequest presignRequest = PresignPutObjectRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .putObjectRequest(put)
                    .build();

            PresignedPutObjectRequest req = presigner.presignPutObject(presignRequest);
            URL url = req.url();

            return Map.of(
                    "url", url.toString(),
                    "key", key,
                    "expiresIn", 900
            );
        }
    }
}