package com.aws.s3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

// Mudar para o nome do seu bucket
    private static final String BUCKET =
            "amzn-s3-java00-bucket-606103596924-us-east-2-an";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void upload(MultipartFile file) throws IOException {

// alterar para a pasta do seu diretorio no s3

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key("Java Class/" + file.getOriginalFilename())
                .build();

        s3Client.putObject(
                request,
                software.amazon.awssdk.core.sync.RequestBody
                        .fromBytes(file.getBytes())
        );
    }

    public List<String> listFiles() {

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(BUCKET)
                .prefix("Java Class/")
                .build();

        ListObjectsV2Response response =
                s3Client.listObjectsV2(request);

        return response.contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }

    public void salvarLogJson(String tipo, String payloadJson) {
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        String chaveArquivo = "logs/" + tipo + "/" + timestamp + "-" + UUID.randomUUID() + ".json";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(chaveArquivo)
                .contentType("application/json")
                .build();

        s3Client.putObject(
                request,
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(payloadJson.getBytes(StandardCharsets.UTF_8))
        );
    }
}
