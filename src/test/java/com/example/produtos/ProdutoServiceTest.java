package com.example.produtos;

import com.example.produtos.api.produto.Produto;
import com.example.produtos.api.produto.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = ProdutosApplication.class) //indica que esta é uma classe de teste para o Spring Boot
@ActiveProfiles("test") //indica o profile que o Spring Boot deve utilizar para passar os testes
public class ProdutoServiceTest {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private ProdutoService service;

    @Test
    public void testGetProdutos() {
        var pageable = PageRequest.of(0, 50);
        var produtos = service.getProdutos(pageable);
        assertEquals(5, produtos.getContent().size());
    }

    @Test
    public void testGetProdutoById(){
        var p = service.getProdutoById(1L);
        assertNotNull(p);
        assertEquals("Café", p.get().getNome());
    }

    @Test
    public void getProdutosByNome(){
        assertEquals(1, service.getProdutosByNome("Café").size());
        assertEquals(1, service.getProdutosByNome("Arroz").size());
        assertEquals(1, service.getProdutosByNome("Feijão").size());
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

        //insere o produto na base da dados
        var p = service.insert(produto);

        //se inseriu
        assertNotNull(p);

        //confirma se o produto foi realmente inserido na base de dados
        Long id = p.getId();
        assertNotNull(id);
        p = service.getProdutoById(id).get();
        assertNotNull(p);

        //compara os valores inseridos com os valores pesquisados para confirmar
        assertEquals("Teste", p.getNome());
        assertEquals("Desc. do produto Teste", p.getDescricao());
        assertEquals(new BigDecimal("10.00"), p.getValorDeVenda());
        assertEquals(Integer.valueOf(100), p.getEstoque());
        assertEquals(Boolean.TRUE, p.getSituacao());

        //Deleta o objeto
        service.delete(id);
        //Verifica se deletou
        if(service.getProdutoById(id).isPresent()){
            fail("O produto não foi excluído");
        }
    }

    @Test
    public void TestUpdate(){
        var p = service.getProdutoById(1L).get();
        var nome = p.getNome(); //armazena o valor original para voltar na base
        p.setNome("Café modificado");
        p.setValorDeCompra(new BigDecimal("5.00"));

        var pDTO = service.update(p, p.getId());
        assertNotNull(pDTO);
        assertEquals("Café modificado", pDTO.getNome());

        //volta ao valor original
        p.setNome(nome);
        pDTO = service.update(p, p.getId());
        assertNotNull(pDTO);
    }

    @Test
    public void testDelete(){
        //cria o produto para teste
        var produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);

        //insere o produto na base da dados
        var p = service.insert(produto);

        //verifica se inseriu
        assertNotNull(p);

        //confirma se o produto foi realmente inserido na base de dados
        Long id = p.getId();
        assertNotNull(id);
        p = service.getProdutoById(id).get();
        assertNotNull(p);

        //Deleta o objeto
        service.delete(id);
        //Verifica se deletou
        if(service.getProdutoById(id).isPresent()){
            fail("O produto não foi excluído");
        }
    }
}
