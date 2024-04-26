package com.example.produtos.api.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de serviço de dados
public class UsuarioService {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private UsuarioRepository rep;

}
