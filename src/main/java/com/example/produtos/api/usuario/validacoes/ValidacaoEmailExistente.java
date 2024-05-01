package com.example.produtos.api.usuario.validacoes;

import com.example.produtos.api.infra.exception.ValidacaoException;
import com.example.produtos.api.usuario.Usuario;
import com.example.produtos.api.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoEmailExistente implements ValidacaoCadastroDeUsuario{
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private UsuarioRepository rep;

    @Override
    public void validar(Usuario usuario) {
        if (rep.existsByEmail(usuario.getEmail())) {
            throw new ValidacaoException("Email já está cadastrado.");
        }
    }
}
