package com.example.produtos.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class TratadorDeExcecoes {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return build(HttpStatus.BAD_REQUEST, "Erro de validação", errors, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPadrao> handleNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado", ex.getMostSpecificCause().getMessage(), request);
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ErroPadrao> handleNotFound(RuntimeException ex, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ErroPadrao> handleAuth(RuntimeException ex, WebRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", ex.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroPadrao> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return build(HttpStatus.FORBIDDEN, "Acesso negado", ex.getMessage(), request);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ErroPadrao> handleInvalidToken(TokenInvalidoException ex, WebRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado", ex.getMessage(), request);
    }

    @ExceptionHandler(ValidacaoEmailJaCadastradoException.class)
    public ResponseEntity<ErroPadrao> handleEmailJaCadastrado(ValidacaoEmailJaCadastradoException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, "Email já cadastrado", ex.getMessage(), request);
    }

    @ExceptionHandler(ValidacaoEmailAindaNaoConfirmadoException.class)
    public ResponseEntity<ErroPadrao> handleEmailNaoConfirmado(ValidacaoEmailAindaNaoConfirmadoException ex, WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Email ainda não confirmado", ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPadrao> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, "Violação de integridade de dados", ex.getMostSpecificCause().getMessage(), request);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ErroPadrao> handleBadRequest(Exception ex, WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Parâmetros de requisição inválidos", ex.getMessage(), request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroPadrao> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado", ex.getMessage(), request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroPadrao> handleMediaType(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Tipo de mídia não suportado", ex.getMessage(), request);
    }

    private ResponseEntity<ErroPadrao> build(HttpStatus status, String error, Object details, WebRequest request) {
        String path = null;
        if (request instanceof ServletWebRequest swr) {
            path = swr.getRequest().getRequestURI();
        }
        var body = new ErroPadrao(Instant.now().toString(), status.value(), error, String.valueOf(details), path);
        return ResponseEntity.status(status).body(body);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ErroPadrao {
        private String timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }
}
