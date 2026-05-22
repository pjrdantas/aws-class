package com.aws.distancia.service;

import com.aws.distancia.dto.DistanciaRequestDto;
import com.aws.distancia.dto.DistanciaResponseDto;
import com.aws.distancia.dto.PontoDto;
import com.aws.shared.exception.CampoObrigatorioException;
import com.aws.shared.exception.LocalidadeVaziaException;

import org.springframework.stereotype.Service;

@Service
public class CalcularDistanciaUseCase {

    private static final double RAIO_TERRA_METROS = 6_371_000;
    private static final String MENSAGEM_SUCESSO = "Distancia calculada com sucesso";

    public DistanciaResponseDto executar(DistanciaRequestDto request) {
        validarRequest(request);

        double distanciaMetros = calcularDistanciaEmMetros(request.pontoA(), request.pontoB());
        return new DistanciaResponseDto(distanciaMetros, MENSAGEM_SUCESSO);
    }

    private void validarRequest(DistanciaRequestDto request) {
        if (request == null || request.pontoA() == null || request.pontoB() == null) {
            throw new LocalidadeVaziaException("As coordenadas devem ser informadas.");
        }

        validarPonto(request.pontoA());
        validarPonto(request.pontoB());
    }

    private void validarPonto(PontoDto ponto) {
        if (ponto.latitude() == null) {
            throw new CampoObrigatorioException("A latitude deve ser informada.");
        }

        if (ponto.longitude() == null) {
            throw new CampoObrigatorioException("A longitude deve ser informada.");
        }
    }

    private double calcularDistanciaEmMetros(PontoDto pontoA, PontoDto pontoB) {
        double latitudeA = Math.toRadians(pontoA.latitude());
        double latitudeB = Math.toRadians(pontoB.latitude());
        double deltaLatitude = Math.toRadians(pontoB.latitude() - pontoA.latitude());
        double deltaLongitude = Math.toRadians(pontoB.longitude() - pontoA.longitude());

        double haversine = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                + Math.cos(latitudeA) * Math.cos(latitudeB)
                * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);

        double distanciaAngular = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
        return RAIO_TERRA_METROS * distanciaAngular;
    }
}
