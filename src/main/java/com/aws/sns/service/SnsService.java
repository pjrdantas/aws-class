package com.aws.sns.service;

import com.aws.sns.dto.SnsPublicarMensagemResponseDto;
import com.aws.shared.exception.CampoObrigatorioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SnsService {

    private final SnsClient snsClient;
    private final String topicArn;

    public SnsService(
            SnsClient snsClient,
            @Value("${aws.sns.topic-arn}") String topicArn
    ) {
        this.snsClient = snsClient;
        this.topicArn = topicArn;
    }

    public SnsPublicarMensagemResponseDto publicar(String assunto, String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            throw new CampoObrigatorioException("O campo mensagem deve ser informado.");
        }

        PublishRequest.Builder requestBuilder = PublishRequest.builder()
                .topicArn(topicArn)
                .message(mensagem);

        if (assunto != null && !assunto.isBlank()) {
            requestBuilder.subject(assunto);
        }

        PublishResponse response = snsClient.publish(requestBuilder.build());

        return new SnsPublicarMensagemResponseDto(
                response.messageId(),
                topicArn,
                "Mensagem publicada com sucesso"
        );
    }
}
