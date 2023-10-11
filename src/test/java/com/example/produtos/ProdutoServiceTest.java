package com.example.produtos;

import com.example.produtos.api.infra.exception.ObjectNotFoundException;
import com.example.produtos.api.produtos.Produto;
import com.example.produtos.api.produtos.ProdutoDTO;
import com.example.produtos.api.produtos.ProdutoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Não substitua pelo banco de dados em memória
//@ActiveProfiles("test") //qual o profile ele deve utilizar para passar os testes
public class ProdutoServiceTest {

    @Autowired
    private ProdutoService service;

    @Test
    public void testGetProdutos() {
        List<ProdutoDTO> produtos = service.getProdutos();
        assertEquals(5, produtos.size());
    }

    @Test
    public void testGetProdutoById(){
        ProdutoDTO p = service.getProdutoById(1L);
        assertNotNull(p);
        assertEquals("Café", p.getNome());
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
        Produto produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);

        //insere o produto na base da dados
        ProdutoDTO p = service.insert(produto);

        //se inseriu
        assertNotNull(p);

        //confirma se o produto foi realmente inserido na base de dados
        Long id = p.getId();
        assertNotNull(id);
        p = service.getProdutoById(id);
        assertNotNull(p);

        //compara os valores inseridos com os valores pesquisados para confirmar
        assertEquals("Teste", p.getNome());
        assertEquals("Desc. do produto Teste", p.getDescricao());
        assertEquals(new BigDecimal("10.00"), p.getValorDeVenda());
        assertEquals(Integer.valueOf(100), p.getEstoque());
        assertEquals(Boolean.TRUE, p.getSituacao());

        // Deletar o objeto
        service.delete(id);
        //Verificar se deletou
        //Verificar se deletou
        if(service.getProdutoById(id) != null){
            fail("O produto não foi excluído");
        }
    }

    @Test
    public void TestUpdate(){
        ProdutoDTO pDTO = service.getProdutoById(1L);
        String nome = pDTO.getNome(); //armazena o valor original para voltar na base
        pDTO.setNome("Café modificado");
        Produto p = Produto.create(pDTO);
        p.setValorDeCompra(new BigDecimal("5.00"));

        pDTO = service.update(p, p.getId());
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
        Produto produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);

        //insere o produto na base da dados
        ProdutoDTO p = service.insert(produto);

        //se inseriu
        assertNotNull(p);

        //confirma se o produto foi realmente inserido na base de dados
        Long id = p.getId();
        assertNotNull(id);
        p = service.getProdutoById(id);
        assertNotNull(p);

        // Deletar o objeto
        service.delete(id);
        //Verificar se deletou
        if(service.getProdutoById(id) != null){
            fail("O produto não foi excluído");
        }
    }
}
