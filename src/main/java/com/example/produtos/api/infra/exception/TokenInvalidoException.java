package com.example.produtos.api.infra.exception;

public class TokenInvalidoException extends RuntimeException{
    public TokenInvalidoException(String mensagem) {
        super(mensagem);
    }
}
