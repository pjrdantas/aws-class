package com.aws.usecase;

import com.aws.dto.DistanciaRequestDto;
import com.aws.dto.DistanciaResponseDto;
import com.aws.dto.PontoDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    void deveRetornarWarnQuandoRequestForVazio() {
        DistanciaResponseDto response = useCase.executar(null);

        assertThat(response.distanciaMetros()).isNull();
        assertThat(response.mensagem()).contains("nao podem vir vazios");
    }

    private org.assertj.core.data.Offset<Double> withinOneMeter() {
        return org.assertj.core.data.Offset.offset(1.0);
    }
}
