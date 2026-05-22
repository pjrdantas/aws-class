package com.aws.sqs.service;

import com.aws.shared.exception.CampoObrigatorioException;
import com.aws.sqs.dto.SqsEnviarMensagemResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SqsService {

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final String queueArn;

    public SqsService(
            SqsClient sqsClient,
            @Value("${aws.sqs.queue-url}") String queueUrl,
            @Value("${aws.sqs.queue-arn}") String queueArn
    ) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.queueArn = queueArn;
    }

    public SqsEnviarMensagemResponseDto enviar(String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            throw new CampoObrigatorioException("O campo mensagem deve ser informado.");
        }

        SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(mensagem)
                .build());

        return new SqsEnviarMensagemResponseDto(
                response.messageId(),
                queueUrl,
                queueArn,
                "Mensagem enviada com sucesso"
        );
    }
}
