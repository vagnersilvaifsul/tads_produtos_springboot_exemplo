package com.example.produtos.api.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link com.example.produtos.api.usuario.Usuario}
 */
public record UsuarioAutenticacaoDTO(
        @Email(message = "O email deve ter @ e  . , no mínimo.")
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        String email,
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        String senha) {
}
