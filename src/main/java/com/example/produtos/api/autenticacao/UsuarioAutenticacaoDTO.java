package com.example.produtos.api.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for {@link com.example.produtos.api.usuario.Usuario}
 */
public record UsuarioAutenticacaoDTO(
        @Email(message = "O email deve ter @ e  . , no mínimo.")
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        String email,
        @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$", message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um númeral, um caractere especial e um total de 8 caracteres.")
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        String senha) {
}
