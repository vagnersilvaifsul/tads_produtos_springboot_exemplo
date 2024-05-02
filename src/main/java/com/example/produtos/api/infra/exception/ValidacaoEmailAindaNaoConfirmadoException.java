package com.example.produtos.api.infra.exception;

public class ValidacaoEmailAindaNaoConfirmadoException extends RuntimeException {
    public ValidacaoEmailAindaNaoConfirmadoException(String message) {
        super(message);
    }
}
