package com.example.produtos.api.usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/perfis")
public class PerfilController {

    private final PerfilRepository repository;

    public PerfilController(PerfilRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody PerfilDTO perfilDTO, UriComponentsBuilder uriBuilder) {
        var perfil = new Perfil();
        perfil.setNome(perfilDTO.nome());
        var u = repository.save(perfil);
        var location = uriBuilder.path("api/v1/perfis/{id}").buildAndExpand(u.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
