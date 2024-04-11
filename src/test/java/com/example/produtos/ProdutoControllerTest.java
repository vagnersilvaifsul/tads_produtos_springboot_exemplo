package com.example.produtos;


import com.example.produtos.api.produto.Produto;
import com.example.produtos.api.produto.ProdutoDTOResponse;
import org.junit.jupiter.api.Test; //jupiter indica que é JUnit 5
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ProdutosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //indica que esta é uma classe de teste para o Spring Boot
@ActiveProfiles("test") //indica o profile que o Spring Boot deve utilizar para passar os testes
public class ProdutoControllerTest extends BaseAPITest {

    //Métodos utilitários
    private ResponseEntity<ProdutoDTOResponse> getProduto(String url) {
        return get(url, ProdutoDTOResponse.class);
    }

    private ResponseEntity<CustomPageImpl<ProdutoDTOResponse>> getProdutosPageble(String url) {
        var headers = getHeaders();

        return rest.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {});
    }

    private ResponseEntity<List<ProdutoDTOResponse>> getProdutosList(String url) {
        var headers = getHeaders();

        return rest.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {});
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void selectAll() { //O nome do método de teste é importante porque deve transmitir a essência do que ele verifica. Este não é um requisito técnico, mas sim uma oportunidade de capturar informações
        var page = getProdutosPageble("/api/v1/produtos").getBody();
        assertNotNull(page);
        assertEquals(5, page.stream().count());

        page = getProdutosPageble("/api/v1/produtos?page=0&size=5").getBody();
        assertNotNull(page);
        assertEquals(5, page.stream().count());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void selectByNome() {
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/arroz").getBody().size());
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/cafe").getBody().size());
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/feijao").getBody().size());

        assertEquals(HttpStatus.NO_CONTENT, getProdutosList("/api/v1/produtos/nome/xxx").getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void selectById() {
        assertNotNull(getProduto("/api/v1/produtos/1"));
        assertNotNull(getProduto("/api/v1/produtos/2"));
        assertNotNull(getProduto("/api/v1/produtos/3"));
        assertEquals(HttpStatus.NOT_FOUND, getProduto("/api/v1/produtos/1000").getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testInsert() {
        //cria o produto para teste
        var produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);

        //Insert
        var response = post("/api/v1/produtos", produto, null);

        //Verifica se criou
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //Busca o objeto
        var location = response.getHeaders().get("location").get(0);
        var p = getProduto(location).getBody();

        assertNotNull(p);
        assertEquals("Teste", p.nome());
        assertEquals(Integer.valueOf(100), p.estoque());

        //Deleta o objeto
        delete(location, null);

        //Verifica se deletou
        assertEquals(HttpStatus.NOT_FOUND, getProduto(location).getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testUpdate() {
        //cria o produto para teste
        var produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);

        //Insert
        var response = post("/api/v1/produtos", produto, null);

        //Verifica se criou
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //Busca o objeto
        var location = response.getHeaders().get("location").get(0);
        ProdutoDTOResponse p = getProduto(location).getBody();

        assertNotNull(p);
        assertEquals("Teste", p.nome());
        assertEquals(Integer.valueOf(100), p.estoque());

        //depois altera seu valor
        var pa = Produto.create(p);
        pa.setEstoque(500);

        //Update
        put("/api/v1/produtos/" + p.id(), pa, null);
        assertEquals(Integer.valueOf(500), pa.getEstoque());

        //Deleta o objeto
        delete(location, null);

        //Verifica se deletou
        assertEquals(HttpStatus.NOT_FOUND, getProduto(location).getStatusCode());

    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testDelete() {
        this.testInsert();
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testGetNotFound() {
        var response = getProduto("/api/v1/produtos/1100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}