package com.example.produtos.api.usuario.validacoes;

import com.example.produtos.api.autenticacao.UsuarioDTO;

public interface ValidacaoLoginDoUsuario {
    void validar(UsuarioDTO usuarioDTO);
}
