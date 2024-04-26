package com.example.produtos.api.autenticacao;

import com.example.produtos.api.infra.security.TokenJwtDTO;
import com.example.produtos.api.infra.security.TokenService;
import com.example.produtos.api.usuario.Usuario;
import com.example.produtos.api.usuario.UsuarioDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("api/v1/login") //Endpoint padrão da classe
public class AutenticacaoController {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private AuthenticationManager manager; //o gerenciador de autenticação é quem dispara o loadUserByUsername (isto é, é interno do Spring Security, tem que usar ele)

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenJwtDTO> efetuaLogin(@RequestBody @Valid UsuarioDTO data){
        var authenticationDTO = new UsernamePasswordAuthenticationToken(data.usuario(), data.senha()); //converte o DTO em DTO do Spring Security
        var authentication = manager.authenticate(authenticationDTO); //autentica o usuário (esse objeto contém o usuário e a senha)
        var tokenJWT = tokenService.geraToken((Usuario) authentication.getPrincipal()); //gera o token JWT para enviar na response
        return ResponseEntity.ok(new TokenJwtDTO(tokenJWT)); //envia a response com o token JWT
    }
}
