package com.aws.sns.controller;

import com.aws.sns.dto.SnsPublicarMensagemRequestDto;
import com.aws.sns.dto.SnsPublicarMensagemResponseDto;
import com.aws.sns.service.SnsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sns")
public class SnsController {

    private final SnsService service;

    public SnsController(SnsService service) {
        this.service = service;
    }

    @PostMapping("/publicar")
    public ResponseEntity<SnsPublicarMensagemResponseDto> publicar(
            @RequestBody SnsPublicarMensagemRequestDto request
    ) {
        return ResponseEntity.ok(service.publicar(request.assunto(), request.mensagem()));
    }
}
