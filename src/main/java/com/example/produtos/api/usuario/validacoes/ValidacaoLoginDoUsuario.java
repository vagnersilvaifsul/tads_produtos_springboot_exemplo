package com.example.produtos.api.usuario.validacoes;

import com.example.produtos.api.autenticacao.UsuarioDTO;
import com.example.produtos.api.usuario.Usuario;

public interface ValidacaoLoginDoUsuario {
    void validar(UsuarioDTO usuarioDTO);
}
