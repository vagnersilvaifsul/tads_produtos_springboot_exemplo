package com.example.produtos.api.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("/api/v1/usuarios") //Endpoint padrão da classe
public class UsuarioController {
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private UsuarioService service;


}