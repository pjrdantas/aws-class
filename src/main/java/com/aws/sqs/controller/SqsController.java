package com.aws.sqs.controller;

import com.aws.sqs.dto.SqsEnviarMensagemRequestDto;
import com.aws.sqs.dto.SqsEnviarMensagemResponseDto;
import com.aws.sqs.service.SqsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
public class SqsController {

    private final SqsService service;

    public SqsController(SqsService service) {
        this.service = service;
    }

    @PostMapping("/enviar")
    public ResponseEntity<SqsEnviarMensagemResponseDto> enviar(
            @RequestBody SqsEnviarMensagemRequestDto request
    ) {
        return ResponseEntity.ok(service.enviar(request.mensagem()));
    }
}
