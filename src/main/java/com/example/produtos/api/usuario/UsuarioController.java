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

/*
    ### Algumas palavras sobre senhas (ao cadastrar um usuário você deve se preocupar com isso, questão de segurança)
    Vá no banco de dados e observe o hash gerada pelo BCryptPasswordEncoder, note que ele se divide em 4 partes:
        $2a$10$W8kA26PmcoqUZx4nle3h9uSGksu1bLmYLUF4H10jb.dlj2FEdLCCa (para senha 123)
    separadas pelos caracteres "$" e "." . Onde:
    $2a -> representa o algoritmo utilizado para encriptação e sua versão.
    $10 -> fator de trabalho (ou força de trabalho, ou rounds)
    $W8kA26PmcoqUZx4nle3h9uSGksu1bLmYLUF4H10jb.dlj2FEdLCCa -> salt + Checksum, um número aleatório fortemente criptografado que deve produzir resultados não determinísticos.

    *Salt: Em vez de usar apenas a senha como entrada para a função hash, bytes aleatórios (conhecidos como salt) são
    gerados para a senha de cada usuário.

    ## Detalhes sobre desempenho: Qual método de validação de senha utilizar?
    Como as funções unidirecionais adaptativas (que gera uma hash para salvar no banco de dados) consomem,
    intencionalmente, muitos recursos, a validação de um nome de usuário e uma senha para cada solicitação pode
    degradar significativamente o desempenho de um aplicativo. Não há nada que o Spring Security (ou qualquer outra
    biblioteca) possa fazer para acelerar a validação da senha, uma vez que a segurança é obtida ao tornar o recurso
    de validação intensivo. Nesse sentido, os desenvolvedores são incentivados a trocar as credenciais de longo prazo
    (ou seja, que exigem o usuário e a senha para processar cada requisição, como, HTTPBasic) por uma credencial de
    curto prazo (como um Token JWT ou um Token OAuth, e assim por diante). A credencial de curto prazo pode ser
    validada rapidamente sem qualquer perda de segurança (desde que o token seja mantido em segredo, é claro :-) ).

    ## O 10 é a força de trabalho em new BCryptPasswordEncoder(10): Quanto maior o parâmetro de força, mais trabalho
    terá que ser feito (exponencialmente) para fazer o hash das senhas. O valor padrão é 10. Esperimente colocar,
    por exemplo, 16, e verá que demora mais a validação da senha.

    Para mais detalhes sobre senhas, consulte:
    https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
    e
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html

 */



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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); //leia o comentário acima da classe para entender mais
        var usuario = new Usuario();
        usuario.setUsuario(usuarioDTO.usuario());
        usuario.setSenha(encoder.encode(usuarioDTO.senha()));
        usuario.setPerfis(Arrays.asList(perfilRepository.findByNome("ROLE_USER")));
        var p = service.insert(usuario);
        var location = uriBuilder.path("api/v1/usuarios/cadastrar/{id}").buildAndExpand(p.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}