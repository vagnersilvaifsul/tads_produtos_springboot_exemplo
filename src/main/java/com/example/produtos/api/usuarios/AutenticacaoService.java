package com.example.produtos.api.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de serviço de dados
public class AutenticacaoService implements UserDetailsService {
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private AutenticacaoRepository rep;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return rep.findByUsuario(username);
    }
}
