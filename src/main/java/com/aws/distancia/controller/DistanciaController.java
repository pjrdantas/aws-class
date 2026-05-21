package com.aws.distancia.controller;

import com.aws.distancia.dto.DistanciaRequestDto;
import com.aws.distancia.dto.DistanciaResponseDto;
import com.aws.distancia.usercase.CalcularDistanciaUseCase;
import com.aws.s3.service.S3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistanciaController {

    private final CalcularDistanciaUseCase calcularDistanciaUseCase;
    private final S3Service s3Service;
    private final ObjectMapper objectMapper;

    public DistanciaController(CalcularDistanciaUseCase calcularDistanciaUseCase, S3Service s3Service, ObjectMapper objectMapper) {
        this.calcularDistanciaUseCase = calcularDistanciaUseCase;
        this.s3Service = s3Service;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/distancia")
    public ResponseEntity<DistanciaResponseDto> calcularDistancia(@RequestBody(required = false) DistanciaRequestDto request) {
        DistanciaResponseDto response = calcularDistanciaUseCase.executar(request);

        try {
            s3Service.salvarLogJson("sucesso", objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException | RuntimeException ignored) {
            // Log em S3 nao deve impedir a resposta principal da API
        }

        return ResponseEntity.ok(response);
    }
}
