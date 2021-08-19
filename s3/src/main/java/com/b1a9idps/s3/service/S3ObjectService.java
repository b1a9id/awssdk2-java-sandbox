package com.b1a9idps.s3.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class S3ObjectService {
    private final S3Client s3Client = S3Client.builder()
            .credentialsProvider(ProfileCredentialsProvider.builder().profileName("develop-ruchitate").build())
            .build();

    /**
     * オブジェクトのリストを取得
     */
    public void list(String bucketName, String prefix) {
        try {
            var response = s3Client.listObjects(builder -> builder.bucket(bucketName).prefix(prefix).build());
            response.contents()
                    .forEach(content -> {
                        long size = content.size() / 1024;

                        System.out.print("=====================================");
                        System.out.print("\n The name of the key is " + content.key());
                        System.out.print("\n The object is " + size + " KBs");
                        System.out.print("\n The owner is " + content.owner());
                        System.out.print("\n=====================================");
                        System.out.println();
                    });
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    /**
     * オブジェクトをダウンロード
     */
    public void getObject(String bucketName, String key) {
        var s3ObjectResponseInputStream =
                s3Client.getObject(builder -> builder.bucket(bucketName).key(key).build(), ResponseTransformer.toInputStream());
        try {
            Path tempFilePath = Files.createTempDirectory(bucketName).resolve("test.zip");
            Files.write(tempFilePath, s3ObjectResponseInputStream.readAllBytes());
            System.out.println(tempFilePath);
        } catch (IOException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
    }
}
