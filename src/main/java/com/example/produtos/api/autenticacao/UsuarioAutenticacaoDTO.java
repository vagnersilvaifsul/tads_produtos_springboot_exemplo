package com.example.produtos.api.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link com.example.produtos.api.usuario.Usuario}
 */
public record UsuarioAutenticacaoDTO(
    @Email @NotBlank
    String email,
    @NotBlank
    String senha) {
}
