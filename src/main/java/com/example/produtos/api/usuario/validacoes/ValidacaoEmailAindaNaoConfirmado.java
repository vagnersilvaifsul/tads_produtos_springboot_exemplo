package com.example.produtos.api.usuario.validacoes;

import com.example.produtos.api.autenticacao.UsuarioDTO;
import com.example.produtos.api.infra.exception.ValidacaoEmailAindaNaoConfirmadoException;
import com.example.produtos.api.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoEmailAindaNaoConfirmado implements ValidacaoLoginDoUsuario{
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private UsuarioRepository rep;

    @Override
    public void validar(UsuarioDTO usuarioDTO) {
        if (!rep.findByUsuario(usuarioDTO.usuario()).isConfirmado()){
            throw new ValidacaoEmailAindaNaoConfirmadoException("Erro: Este email ainda não foi confirmado. Favor acessar a caixa de email e clicar no link para confirmar.");
        }
    }
}
