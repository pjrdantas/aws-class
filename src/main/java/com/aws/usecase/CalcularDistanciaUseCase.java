package com.aws.usecase;

import com.aws.dto.DistanciaRequestDto;
import com.aws.dto.DistanciaResponseDto;
import com.aws.dto.PontoDto;
import org.springframework.stereotype.Service;

@Service
public class CalcularDistanciaUseCase {

    private static final double RAIO_TERRA_METROS = 6_371_000;
    private static final String WARN_CAMPOS_OBRIGATORIOS =
            "pontoA.latitude, pontoA.longitude, pontoB.latitude e pontoB.longitude nao podem vir vazios, senao nao consegue calcular";
    private static final String MENSAGEM_SUCESSO = "Distancia calculada com sucesso";

    public DistanciaResponseDto executar(DistanciaRequestDto request) {
        if (possuiCampoVazio(request)) {
            return new DistanciaResponseDto(null, WARN_CAMPOS_OBRIGATORIOS);
        }

        double distanciaMetros = calcularDistanciaEmMetros(request.pontoA(), request.pontoB());
        return new DistanciaResponseDto(distanciaMetros, MENSAGEM_SUCESSO);
    }

    private boolean possuiCampoVazio(DistanciaRequestDto request) {
        return request == null
                || request.pontoA() == null
                || request.pontoB() == null
                || request.pontoA().latitude() == null
                || request.pontoA().longitude() == null
                || request.pontoB().latitude() == null
                || request.pontoB().longitude() == null;
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
