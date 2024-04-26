package com.example.produtos.api.usuario;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@RestController //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("/api/v1/usuarios") //Endpoint padrão da classe
public class UsuarioController {
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private UsuarioService service;
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private PerfilRepository perfilRepository;

    @PostMapping(path = "/cadastrar")
    @Transactional
    public ResponseEntity<URI> cadastrar(@RequestBody @Valid UsuarioDTO usuarioDTO, UriComponentsBuilder uriBuilder){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        var usuario = new Usuario();
        usuario.setUsuario(usuarioDTO.usuario());
        usuario.setSenha(encoder.encode(usuarioDTO.senha()));
        usuario.setPerfis(Arrays.asList(perfilRepository.findByNome("ROLE_USER")));
        var p = service.insert(usuario);
        var location = uriBuilder.path("api/v1/usuarios/cadastrar/{id}").buildAndExpand(p.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}