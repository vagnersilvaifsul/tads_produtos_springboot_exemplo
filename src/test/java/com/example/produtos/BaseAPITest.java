package com.example.produtos;

import com.example.produtos.api.infra.security.jwt.JwtUtil;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(classes = ProdutosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseAPITest {
    @Autowired
    protected TestRestTemplate rest;

    @Autowired
    @Qualifier("userDetailsService")
    protected UserDetailsService userDetailsService;

    private String jwtToken = "";

    //Método utilitário para formatar o header da requisição
    HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        return headers;
    }

    //Método para requisitar o token para um user especificado
    @Before //essa anotação faz com que o método seja executado antes de qualquel requisição
    public void setupTest() {
        // Le usuário
        UserDetails user = userDetailsService.loadUserByUsername("admin");
        assertNotNull(user);

        // Gera token
        jwtToken = JwtUtil.createToken(user);
        System.out.println("jwtToken gerado:");
        System.out.println(jwtToken);
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