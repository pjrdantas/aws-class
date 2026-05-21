package com.aws.shared.exception;

import com.aws.s3.service.S3Service;
import com.aws.shared.dto.ErroResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final S3Service s3Service;
    private final ObjectMapper objectMapper;

    public ApiExceptionHandler(S3Service s3Service, ObjectMapper objectMapper) {
        this.s3Service = s3Service;
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler({CampoObrigatorioException.class, LocalidadeVaziaException.class})
    public ResponseEntity<ErroResponseDto> tratarBadRequest(RuntimeException exception, HttpServletRequest request) {
        return montarResposta(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDto> tratarJsonInvalido(HttpServletRequest request) {
        return montarResposta(HttpStatus.BAD_REQUEST, "As coordenadas devem ser informadas.", request.getRequestURI());
    }

    @ExceptionHandler({RecursoNaoEncontradoException.class, NoResourceFoundException.class})
    public ResponseEntity<ErroResponseDto> tratarNotFound(Exception exception, HttpServletRequest request) {
        String mensagem = exception instanceof RecursoNaoEncontradoException
                ? exception.getMessage()
                : "Recurso nao encontrado.";

        return montarResposta(HttpStatus.NOT_FOUND, mensagem, request.getRequestURI());
    }

    @ExceptionHandler(RecursoJaExisteException.class)
    public ResponseEntity<ErroResponseDto> tratarConflict(RecursoJaExisteException exception, HttpServletRequest request) {
        return montarResposta(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDto> tratarErroInesperado(HttpServletRequest request) {
        return montarResposta(HttpStatus.BAD_REQUEST, "Nao foi possivel processar a requisicao.", request.getRequestURI());
    }

    private ResponseEntity<ErroResponseDto> montarResposta(HttpStatus status, String mensagem, String caminho) {
        ErroResponseDto erro = new ErroResponseDto(status.value(), status.getReasonPhrase(), mensagem);

        try {
            String payload = objectMapper.writeValueAsString(new LogErroS3(caminho, status.value(), mensagem));
            s3Service.salvarLogJson("exception", payload);
        } catch (JsonProcessingException | RuntimeException ignored) {
            // Log em S3 nao deve quebrar o fluxo da API
        }

        return ResponseEntity.status(status).body(erro);
    }

    private record LogErroS3(String endpoint, Integer status, String mensagem) {
    }
}
