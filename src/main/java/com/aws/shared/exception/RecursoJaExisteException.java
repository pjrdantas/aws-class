package com.aws.shared.exception;

public class RecursoJaExisteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoJaExisteException(String mensagem) {
        super(mensagem);
    }
}
