package com.aws.sns.dto;

public record SnsPublicarMensagemRequestDto(
        String assunto,
        String mensagem
) {
}
