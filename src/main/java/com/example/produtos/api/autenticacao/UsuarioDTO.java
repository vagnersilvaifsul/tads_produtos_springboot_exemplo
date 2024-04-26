package com.example.produtos.api.autenticacao;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank
    String usuario,
    @NotBlank
    String senha) {
}
