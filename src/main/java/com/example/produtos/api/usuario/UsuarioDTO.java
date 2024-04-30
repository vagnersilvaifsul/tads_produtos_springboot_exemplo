package com.example.produtos.api.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank
    String usuario,
    @NotBlank
    String senha,
    @NotBlank
    String nome,
    @NotBlank
    String sobrenome,
    @Email
    String email) {
}
