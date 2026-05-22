package com.aws.sqs.dto;

public record SqsEnviarMensagemResponseDto(
        String messageId,
        String queueUrl,
        String queueArn,
        String mensagem
) {
}
