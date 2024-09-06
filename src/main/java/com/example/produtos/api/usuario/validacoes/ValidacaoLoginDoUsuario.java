package com.example.produtos.api.usuario.validacoes;

import com.example.produtos.api.autenticacao.UsuarioAutenticacaoDTO;

public interface ValidacaoLoginDoUsuario {
    void validar(UsuarioAutenticacaoDTO usuarioAutenticacaoDTO);
}
