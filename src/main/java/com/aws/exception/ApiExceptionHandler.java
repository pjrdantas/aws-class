package com.aws.exception;

import com.aws.dto.ErroResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({CampoObrigatorioException.class, LocalidadeVaziaException.class})
    public ResponseEntity<ErroResponseDto> tratarBadRequest(RuntimeException exception) {
        return montarResposta(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDto> tratarJsonInvalido() {
        return montarResposta(HttpStatus.BAD_REQUEST, "As coordenadas devem ser informadas.");
    }

    @ExceptionHandler({RecursoNaoEncontradoException.class, NoResourceFoundException.class})
    public ResponseEntity<ErroResponseDto> tratarNotFound(Exception exception) {
        String mensagem = exception instanceof RecursoNaoEncontradoException
                ? exception.getMessage()
                : "Recurso nao encontrado.";

        return montarResposta(HttpStatus.NOT_FOUND, mensagem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDto> tratarErroInesperado() {
        return montarResposta(HttpStatus.BAD_REQUEST, "Nao foi possivel processar a requisicao.");
    }

    private ResponseEntity<ErroResponseDto> montarResposta(HttpStatus status, String mensagem) {
        ErroResponseDto erro = new ErroResponseDto(status.value(), status.getReasonPhrase(), mensagem);
        return ResponseEntity.status(status).body(erro);
    }
}
