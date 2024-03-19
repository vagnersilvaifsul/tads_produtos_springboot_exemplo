package com.example.produtos.api.usuarios;

import com.example.produtos.api.infra.security.TokenJwtDTO;
import com.example.produtos.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager; //o gerenciador de autenticação é quem dispara o loadUserByUsername (isto é, é interno do Spring Security, tem que usar ele)

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenJwtDTO> efetuaLogin(@RequestBody @Valid UsuarioDTO data){
        var authenticationDTO = new UsernamePasswordAuthenticationToken(data.usuario(), data.senha()); //converte o DTO em DTO do Spring Security
        var authentication = manager.authenticate(authenticationDTO);
        var tokenJWT = tokenService.geraToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenJwtDTO(tokenJWT));
    }
}
