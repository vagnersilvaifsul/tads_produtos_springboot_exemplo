package com.example.produtos.api.produto;


import com.example.produtos.BaseAPIIntegracaoTest;
import com.example.produtos.CustomPageImpl;
import com.example.produtos.ProdutosApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; //jupiter indica que é JUnit 5
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
    Realiza o teste de integração da unidade ProdutoController.
    Utiliza como dependência principal a classe TestRestTemplate (do Spring), implementada na superclasse, BaseAPIIntegracaoTest.
 */

@SpringBootTest(classes = ProdutosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //indica que vai rodar o teste no container Spring Boot (Por isso é um teste de integração, pois utiliza o ambiente real, ao invés de um Mock)
@ActiveProfiles("test") //indica o profile que o Spring Boot deve utilizar para passar os testes
public class ProdutoControllerIntegracaoTest extends BaseAPIIntegracaoTest {

    //Métodos utilitários (eles encapsulam o TestRestTemplate e eliminam a repetição de código nos casos de teste)
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
    @DisplayName("Espera uma página, testa se tem 5 objetos, busca por página, de tamanho 5, e testa se tem 5 objetos")
    public void selectAllEsperaUmaPaginaCom5ObjetosEUmaPaginaDe5Objetos() { //O nome do método de teste é importante porque deve transmitir a essência do que ele verifica. Este não é um requisito técnico, mas sim uma oportunidade de capturar informações
        // ACT
        var page = getProdutosPageble("/api/v1/produtos").getBody();

        // ASSERT (testa se retorna a quantidade de dados esperada)
        assertNotNull(page);
        assertEquals(5, page.stream().count());

        // ACT
        page = getProdutosPageble("/api/v1/produtos?page=0&size=5").getBody();

        // ASSERT (testa se retorna o tamanho de página solicitado)
        assertNotNull(page);
        assertEquals(5, page.stream().count());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void selectByNomeEsperaUmObjetoPorNomePesquisado() {
        // ACT + ASSERT
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/arroz").getBody().size());
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/erva").getBody().size());
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/cha").getBody().size());
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/cafe").getBody().size());
        assertEquals(1, getProdutosList("/api/v1/produtos/nome/feijao").getBody().size());

        // ACT + ASSERT
        assertEquals(HttpStatus.NO_CONTENT, getProdutosList("/api/v1/produtos/nome/xxx").getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void selectByIdEsperaUmObjetoPorIdPesquisadoENotFoudParaIdInexistente() {
        // ACT + ASSERT
        assertNotNull(getProduto("/api/v1/produtos/1"));
        assertNotNull(getProduto("/api/v1/produtos/2"));
        assertNotNull(getProduto("/api/v1/produtos/3"));
        assertNotNull(getProduto("/api/v1/produtos/4"));
        assertNotNull(getProduto("/api/v1/produtos/5"));
        assertEquals(HttpStatus.NOT_FOUND, getProduto("/api/v1/produtos/100000").getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testInsertEspera204CreatedE404ENotFound() {
        // ARRANGE
        var ProdutoDTOPost = new ProdutoDTOPost(
            "Teste",
            "Desc. do produto Teste",
            new BigDecimal("5.00"),
            new BigDecimal("10.00"),
            100
        );

        // ACT
        var response = post("/api/v1/produtos", ProdutoDTOPost, null);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // ARRANGE
        var location = response.getHeaders().get("location").get(0);
        var p = getProduto(location).getBody();
        assertNotNull(p);
        assertEquals("Teste", p.nome());
        assertEquals("Desc. do produto Teste", p.descricao());
        assertEquals(new BigDecimal("10.00"), p.valorDeVenda());
        assertEquals(Integer.valueOf(100), p.estoque());
        assertEquals(true, p.situacao());
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getProduto(location).getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testUpdateEspera200OkE404ENotFound() {
        // ARRANGE
        var ProdutoDTOPost = new ProdutoDTOPost(
            "Teste",
            "Desc. do produto Teste",
            new BigDecimal("5.00"),
            new BigDecimal("10.00"),
            100
        );

        var responsePost = post("/api/v1/produtos", ProdutoDTOPost, null);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);
        var pDto = getProduto(location).getBody();
        assertNotNull(pDto);
        assertEquals("Teste", pDto.nome());
        assertEquals("Desc. do produto Teste", pDto.descricao());
        assertEquals(new BigDecimal("10.00"), pDto.valorDeVenda());
        assertEquals(Integer.valueOf(100), pDto.estoque());
        assertEquals(true, pDto.situacao());
        //prepara um DTO para o PUT
        var produtoDTOPut = new ProdutoDTOPut(
            "Teste Modificado",
            "Desc. do produto Teste Modificado",
            new BigDecimal("20.00"),
            new BigDecimal("50.00"),
            500,
            Boolean.FALSE
        );

        // ACT
        var responsePUT = put(location, produtoDTOPut, ProdutoDTOResponse.class);

        // ASSERT
        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals("Teste Modificado", responsePUT.getBody().nome());
        assertEquals("Desc. do produto Teste Modificado", responsePUT.getBody().descricao());
        assertEquals(new BigDecimal("50.00"), responsePUT.getBody().valorDeVenda());
        assertEquals(Integer.valueOf(500), responsePUT.getBody().estoque());
        assertEquals(Boolean.FALSE, responsePUT.getBody().situacao());

        // ACT
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getProduto(location).getStatusCode());

    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testDeleteEspera200OkE404NotFound() {
        // ARRANGE
        var produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);
        var responsePost = post("/api/v1/produtos", produto, null);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);
        var p = getProduto(location).getBody();
        assertNotNull(p);
        assertEquals("Teste", p.nome());
        assertEquals(Integer.valueOf(100), p.estoque());

        // ACT
        var responseDelete = delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getProduto(location).getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testGetNotFoundEspera404NotFound() {
        // ARRANGE + ACT
        var response = getProduto("/api/v1/produtos/1100");

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}