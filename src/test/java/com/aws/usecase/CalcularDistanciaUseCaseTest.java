package com.aws.usecase;

import com.aws.distancia.dto.DistanciaRequestDto;
import com.aws.distancia.dto.DistanciaResponseDto;
import com.aws.distancia.dto.PontoDto;
import com.aws.distancia.usercase.CalcularDistanciaUseCase;
import com.aws.shared.exception.CampoObrigatorioException;
import com.aws.shared.exception.LocalidadeVaziaException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalcularDistanciaUseCaseTest {

    private final CalcularDistanciaUseCase useCase = new CalcularDistanciaUseCase();

    @Test
    void deveCalcularDistanciaEmMetros() {
        DistanciaRequestDto request = new DistanciaRequestDto(
                new PontoDto(-23.561684, -46.655981),
                new PontoDto(-22.951916, -43.210487)
        );

        DistanciaResponseDto response = useCase.executar(request);

        assertThat(response.distanciaMetros()).isCloseTo(358450.9, withinOneMeter());
        assertThat(response.mensagem()).isEqualTo("Distancia calculada com sucesso");
    }

    @Test
    void deveLancarExceptionQuandoRequestForVazio() {
        assertThatThrownBy(() -> useCase.executar(null))
                .isInstanceOf(LocalidadeVaziaException.class)
                .hasMessage("As coordenadas devem ser informadas.");
    }

    @Test
    void deveLancarExceptionQuandoLatitudeForVazia() {
        DistanciaRequestDto request = new DistanciaRequestDto(
                new PontoDto(null, -46.655981),
                new PontoDto(-22.951916, -43.210487)
        );

        assertThatThrownBy(() -> useCase.executar(request))
                .isInstanceOf(CampoObrigatorioException.class)
                .hasMessage("A latitude deve ser informada.");
    }

    private org.assertj.core.data.Offset<Double> withinOneMeter() {
        return org.assertj.core.data.Offset.offset(1.0);
    }
}
