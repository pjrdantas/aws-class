package com.aws.shared.exception;

public class LocalidadeVaziaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public LocalidadeVaziaException(String mensagem) {
        super(mensagem);
    }
}
