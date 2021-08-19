package com.b1a9idps.s3;

import com.b1a9idps.s3.service.S3ObjectService;

public class Application {
    public static void main(String[] args) {
        final var s3ObjectService = new S3ObjectService();

        s3ObjectService.list("uchitate-bucket", "aws-sdk2-sandbox");
        s3ObjectService.getObject("uchitate-bucket", "aws-sdk2-sandbox/test.zip");
    }
}
