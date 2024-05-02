package com.example.produtos.api.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @Email
    String email,
    @NotBlank
    String senha) {
}
