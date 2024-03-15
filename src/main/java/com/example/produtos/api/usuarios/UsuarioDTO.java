package com.example.produtos.api.usuarios;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank
    String usuario,
    @NotBlank
    String senha) {
}
