package com.example.produtos.api.infra.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class TratadorDeErros extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity trataErro400(ConstraintViolationException ex){ //400 - Bad Request para Erro de Validação
        var erros = ex.getConstraintViolations();
        //System.out.println(ex);
        return ResponseEntity.badRequest().body(erros.stream().map(ErroValidation::new).toList());
    }

    private record ErroValidation(
        String campo,
        String mensagem) {
        public ErroValidation(ConstraintViolation<?> erro){
            this(erro.getPropertyPath().toString(), erro.getMessage()); //qual campo e qual a mensagem do Validation
        }
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity trataErro404() { //404 - Not Found
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity trataErro400() { //400 - Bad Request
        return ResponseEntity.badRequest().build();
    }
}