package com.aws.controller;

import com.aws.dto.DistanciaRequestDto;
import com.aws.dto.DistanciaResponseDto;
import com.aws.usecase.CalcularDistanciaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistanciaController {

    private final CalcularDistanciaUseCase calcularDistanciaUseCase;

    public DistanciaController(CalcularDistanciaUseCase calcularDistanciaUseCase) {
        this.calcularDistanciaUseCase = calcularDistanciaUseCase;
    }

    @PostMapping("/distancia")
    public ResponseEntity<DistanciaResponseDto> calcularDistancia(@RequestBody(required = false) DistanciaRequestDto request) {
        DistanciaResponseDto response = calcularDistanciaUseCase.executar(request);
        return ResponseEntity.ok(response);
    }
}
