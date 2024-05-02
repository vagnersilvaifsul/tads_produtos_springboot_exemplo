package com.example.produtos.api.infra.exception;

public class ValidacaoEmailJaCadastradoException extends RuntimeException {
    public ValidacaoEmailJaCadastradoException(String message) {
        super(message);
    }
}
