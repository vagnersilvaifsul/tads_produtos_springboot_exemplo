package com.example.produtos.api.usuario.validacoes;

import com.example.produtos.api.infra.exception.ValidacaoEmailJaCadastradoException;
import com.example.produtos.api.usuario.Usuario;
import com.example.produtos.api.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoEmailJaCadastrado implements ValidacaoCadastroDeUsuario{
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private UsuarioRepository rep;

    @Override
    public void validar(Usuario usuario) {
        if (rep.existsByEmail(usuario.getEmail())) {
            throw new ValidacaoEmailJaCadastradoException("Erro: Email já está cadastrado.");
        }
    }
}
