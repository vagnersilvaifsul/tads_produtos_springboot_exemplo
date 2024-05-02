package com.example.produtos.api.infra.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*
    Indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean onde se fará o
    tratamento das exceções lançadas pelos Controllers, como, @RestController.
 */
@RestControllerAdvice //leia o comentário acima para entender essa anotação
public class TratadorDeErros extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity trataErro400(ConstraintViolationException ex){ //400 - Bad Request para Erro de Validação da Validation
        var erros = ex.getConstraintViolations();
        return ResponseEntity.badRequest().body(erros.stream().map(ErroValidation::new).toList());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity trataErro404() { //404 - Not Found
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity trataErro400() { //400 - Bad Request
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ValidacaoEmailJaCadastradoException.class)
    public ResponseEntity trataErro400(ValidacaoEmailJaCadastradoException ex){ //400 - Bad Request para Erro de Validação das Regras de Negócio
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ValidacaoEmailAindaNaoConfirmadoException.class)
    public ResponseEntity trataErro400(ValidacaoEmailAindaNaoConfirmadoException ex){ //400 - Bad Request para Erro de Validação das Regras de Negócio
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private record ErroValidation(
        String campo,
        String mensagem) {
        public ErroValidation(ConstraintViolation<?> erro){
            this(erro.getPropertyPath().toString(), erro.getMessage()); //qual campo e qual a mensagem do Validation
        }
    }
}