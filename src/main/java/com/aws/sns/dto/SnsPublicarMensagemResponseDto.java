package com.aws.sns.dto;

public record SnsPublicarMensagemResponseDto(
        String messageId,
        String topicArn,
        String mensagem
) {
}
