package com.example.produtos;


import com.example.produtos.api.produto.Produto;
import com.example.produtos.api.produto.ProdutoDTOResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProdutosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProdutoControllerTest extends BaseAPITest {

    //Métodos utilitários
    private ResponseEntity<ProdutoDTOResponse> getProduto(String url) {
        return get(url, ProdutoDTOResponse.class);
    }

    private ResponseEntity<List<ProdutoDTOResponse>> getProdutos(String url) {
        var headers = getHeaders();

        return rest.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {
            });
    }

    @Test
    public void selectAll() {
        var produtos = getProdutos("/api/v1/produtos").getBody();
        assertNotNull(produtos);
        assertEquals(5, produtos.size());

        produtos = getProdutos("/api/v1/produtos?page=0&size=5").getBody();
        assertNotNull(produtos);
        assertEquals(5, produtos.size());
    }

    @Test
    public void selectByNome() {

        assertEquals(1, getProdutos("/api/v1/produtos/nome/arroz").getBody().size());
        assertEquals(1, getProdutos("/api/v1/produtos/nome/cafe").getBody().size());
        assertEquals(1, getProdutos("/api/v1/produtos/nome/feijao").getBody().size());

        assertEquals(HttpStatus.NO_CONTENT, getProdutos("/api/v1/produtos/nome/xxx").getStatusCode());
    }

    @Test
    public void selectById() {
        assertNotNull(getProduto("/api/v1/produtos/1"));
        assertNotNull(getProduto("/api/v1/produtos/2"));
        assertNotNull(getProduto("/api/v1/produtos/3"));
        assertEquals(HttpStatus.NOT_FOUND, getProduto("/api/v1/produtos/1000").getStatusCode());
    }

    @Test
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
        System.out.println(response);

        //Verifica se criou
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //Busca o objeto
        var location = response.getHeaders().get("location").get(0);
        var p = getProduto(location).getBody();

        assertNotNull(p);
        assertEquals("Teste", p.getNome());
        assertEquals(Integer.valueOf(100), p.getEstoque());

        //Deleta o objeto
        delete(location, null);

        //Verifica se deletou
        assertEquals(HttpStatus.NOT_FOUND, getProduto(location).getStatusCode());
    }

    @Test
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
        System.out.println(response);

        //Verifica se criou
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //Busca o objeto
        var location = response.getHeaders().get("location").get(0);
        ProdutoDTOResponse p = getProduto(location).getBody();

        assertNotNull(p);
        assertEquals("Teste", p.getNome());
        assertEquals(Integer.valueOf(100), p.getEstoque());

        //depois altera seu valor
        var pa = Produto.create(p);
        pa.setEstoque(500);

        //Update
        response = put("/api/v1/produtos/" + p.getId(), pa, null);
        System.out.println(response);
        assertEquals(Integer.valueOf(500), pa.getEstoque());

        //Deleta o objeto
        delete(location, null);

        //Verifica se deletou
        assertEquals(HttpStatus.NOT_FOUND, getProduto(location).getStatusCode());

    }

    @Test
    public void testDelete() {
        this.testInsert();
    }

    @Test
    public void testGetNotFound() {
        var response = getProduto("/api/v1/produtos/1100");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}