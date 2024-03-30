package com.example.produtos;


import com.example.produtos.api.infra.security.TokenService;
import com.example.produtos.api.usuarios.AutenticacaoService;
import com.example.produtos.api.usuarios.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(classes = ProdutosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseAPITest {
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    protected TestRestTemplate rest;
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private AutenticacaoService service;
    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private TokenService tokenService;

    private String jwtToken = "";

    //Método utilitário para montar o header da requisição
    HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        return headers;
    }

    //Método para requisitar o token para um user especificado
    @BeforeEach //essa anotação faz com que o método seja executado antes de qualquel requisição
    public void setupTest() {
        // Le usuário
        Usuario user = (Usuario) service.loadUserByUsername("admin");
        assertNotNull(user);

        // Gera token
        jwtToken = tokenService.geraToken(user);
        assertNotNull(jwtToken);
    }

    //metodo genérico para o verbo POST
    <T> ResponseEntity<T> post(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = getHeaders();

        return rest.exchange(url, POST, new HttpEntity<>(body, headers), responseType);
    }

    //metodo genérico para o verbo PUT
    <T> ResponseEntity<T> put(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = getHeaders();

        return rest.exchange(url, PUT, new HttpEntity<>(body, headers), responseType);
    }

    //metodo genérico para o verbo GET
    <T> ResponseEntity<T> get(String url, Class<T> responseType) {

        HttpHeaders headers = getHeaders();

        return rest.exchange(url, GET, new HttpEntity<>(headers), responseType);
    }

    //metodo genérico para o verbo DELETE
    <T> ResponseEntity<T> delete(String url, Class<T> responseType) {

        HttpHeaders headers = getHeaders();

        return rest.exchange(url, DELETE, new HttpEntity<>(headers), responseType);
    }
}