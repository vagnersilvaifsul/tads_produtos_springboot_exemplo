package com.example.produtos.api.produto;

import com.example.produtos.ProdutosApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; //jupiter indica que é JUnit 5
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
    Realiza o teste de integração da unidade ProdutoService.

    Exitem dois padrões amplamente aceitos para escrever testes: Triple A e GWT.
    O padrão GWT é aplicado em BDD (Behavior-Driven Development).

    ### Arrange, Act, Assert (AAA)
    O padrão AAA é amplamente utilizado e consiste em três etapas distintas:
    a) Arrange (Preparar): Nesta etapa, são realizadas todas as configurações iniciais necessárias para que o
    cenário de teste possa ser executado. Isso pode incluir a criação de objetos, definição de variáveis,
    configuração de ambiente e qualquer outra preparação necessária para que o teste seja executado em um estado
    específico.
    b) Act (Ação): Nesta fase, a ação que se deseja testar é executada. Pode ser a chamada de um método, uma
    interação com a interface do usuário ou qualquer outra operação que seja o foco do teste.
    c) Assert (Verificar): Na última etapa, os resultados são verificados em relação ao comportamento esperado. É
    onde se avalia se o resultado obtido após a ação está de acordo com o que se esperava do teste. Caso haja alguma
    discrepância entre o resultado real e o esperado, o teste falhará.
 */


@SpringBootTest(classes = ProdutosApplication.class) //indica que vai rodar o teste no container Spring Boot (Por isso é um teste de integração, pois utiliza o ambiente real, ao invés de um Mock)
@ActiveProfiles("test") //indica o profile que o Spring Boot deve utilizar para passar os testes
public class ProdutoServiceIntegracaoTest {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private ProdutoService service;

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    @DisplayName("Busca os produtos na base de dados, espera 5 objetos")
    public void testGetProdutosEsperaUmaPaginaComTamanhoX() { //O nome do método de teste é importante porque deve transmitir a essência do que ele verifica. Este não é um requisito técnico, mas sim uma oportunidade de capturar informações
        // ARRANGE
        var pageable = PageRequest.of(0, 50);

        // ACT
        var produtos = service.getProdutos(pageable);

        // ASSERT
        assertEquals(5, produtos.getContent().size());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testGetProdutoByIdEsperaOProdutoDeId1(){
        // ARRANGE + ACT
        var p = service.getProdutoById(1L);

        // ASSERT
        assertNotNull(p);
        assertEquals("Café", p.get().getNome());
        assertEquals("Café em pó tradicional Igaçu lata 400g", p.get().getDescricao());
        assertEquals(100, p.get().getEstoque());
        assertEquals(true, p.get().getSituacao());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void getProdutosByNomeEsperaUmObjetoPorNomePesquisado(){
        // ARRANGE + ACT + ASSERT
        assertEquals(1, service.getProdutosByNome("Café").size());
        assertEquals(1, service.getProdutosByNome("Erva Mate").size());
        assertEquals(1, service.getProdutosByNome("Chá Preto").size());
        assertEquals(1, service.getProdutosByNome("Arroz").size());
        assertEquals(1, service.getProdutosByNome("Feijão").size());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testInsertEsperaOObjetoInseridoEODeleta() {
        // ARRANGE
        var produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);

        //insere o produto na base da dados
        var p = service.insert(produto);

        // ACT
        assertNotNull(p);

        // ASSERT
        Long id = p.getId();
        assertNotNull(id);
        p = service.getProdutoById(id).get();
        assertNotNull(p); //confirma se o produto foi realmente inserido na base de dados

        //compara os valores inseridos com os valores pesquisados para confirmar
        assertEquals("Teste", p.getNome());
        assertEquals("Desc. do produto Teste", p.getDescricao());
        assertEquals(new BigDecimal("10.00"), p.getValorDeVenda());
        assertEquals(Integer.valueOf(100), p.getEstoque());
        assertEquals(Boolean.TRUE, p.getSituacao());

        //Deleta o objeto (pois está trabalhando com um banco de dados real e, por isso, requer que seja mantida sua consistência para os demais testes)
        service.delete(id);
        //Verifica se deletou
        if(service.getProdutoById(id).isPresent()){
            fail("O produto não foi excluído");
        }
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void TestUpdateEsperaOObjetoAlteradoEODeleta(){
        // ARRANGE
        var p = service.getProdutoById(1L).get();
        var nome = p.getNome(); //armazena o valor original para voltar na base
        p.setNome("Café modificado");
        p.setValorDeCompra(new BigDecimal("5.00"));

        // ACT
        var pDTO = service.update(p, p.getId());
        assertNotNull(pDTO);
        assertEquals("Café modificado", pDTO.getNome());

        //volta ao valor original (para manter a consistência do banco de dados)
        p.setNome(nome);
        pDTO = service.update(p, p.getId());
        assertNotNull(pDTO);
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void testDeleteEsperaAExclusaoDeUmObjetoInserido(){
        // ARRANGE
        var produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);

        // ACT
        var p = service.insert(produto);

        //verifica se inseriu
        assertNotNull(p);

        //confirma se o produto foi realmente inserido na base de dados
        Long id = p.getId();
        assertNotNull(id);
        p = service.getProdutoById(id).get();
        assertNotNull(p);

        //Deleta o objeto (para manter a consistência do banco de dados)
        service.delete(id);
        //Verifica se deletou
        if(service.getProdutoById(id).isPresent()){
            fail("O produto não foi excluído");
        }
    }
}
