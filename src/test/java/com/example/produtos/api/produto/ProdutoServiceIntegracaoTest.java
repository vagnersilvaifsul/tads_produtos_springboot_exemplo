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

/*
    Realiza o teste de integração da unidade ProdutoService.

    Exitem dois padrões amplamente aceitos para escrever testes: Triple A e GWT.
    O padrão GWT (Given-When-Then, em português Dado-Quando-Então. Dado um conjunto de entradas - Quando a aplicação
    faz uma ação X - Então é esperado Y) é aplicado em BDD (Behavior-Driven Development, Desenvolvimento Orientado ao Comportamento).

    ### Arrange, Act, Assert (AAA)
    O padrão AAA é amplamente utilizado e consiste em três etapas distintas:
    a) Arrange (Preparar): Nesta etapa, são realizadas todas as configurações iniciais necessárias para que o
    cenário de teste possa ser executado. Isso pode incluir a criação de objetos, definição de variáveis,
    configuração de ambiente e qualquer outra preparação necessária para que o teste seja executado em um estado
    específico.
    b) Act (Ação): Nesta fase, a ação que se deseja testar é executada. Pode ser a chamada de um método, uma
    interação com a interface do usuário ou qualquer outra operação que seja o foco do teste.
    c) Assert (Afirmar): Na última etapa, os resultados são verificados em relação ao comportamento esperado. É
    onde se avalia se o resultado obtido após a ação está de acordo com o que se esperava do teste. Caso haja alguma
    discrepância entre o resultado real e o esperado, o teste falhará.

    Pequeno Glossário:
    Às vezes a sopa de letrinhas utilizadas na área de desenvolvimento de software pode causar alguma confusão. Para
    esclarecer um pouco mais, segue alguns conceitos das metodologias ágeis:

    + TLD (Test-Last Development): É o Desenvolvimento com Testes Posteriores. Desenvolve-se o software, depois o testa
        para garantir sua qualidade.

    + TDD (Test Driven Development): É o Desenvolvimento Orientado a Testes. Primeiro, cria-se o teste conforme o resultado
        que se deseja atingir para determinada funcionalidade. Depois, se aprimora o código (com refinamentos sussecivos).
        Geralmente é o próprio desenvolvedor que escreve os testes.

    + BDD (Behavior Driven Development): é o Desenvolvimento Orientado ao Comportamento, cujos testes são baseados no
        comportamento do software ao longo da sua vida útil. Com o BDD, a principal diferença é que as equipes de QA
        escrevem os testes, necessários para passar, antes que o desenvolvimento possa marcar um recurso como concluído.

    + DDD (Domain-Driven Design): Design Orientado pelo Domínio é uma abordagem para o desenvolvimento de software que
        enfatiza a importância de entender profundamente o domínio do problema que está sendo resolvido. Utilizada para
        desenvolvimento de softwares complexos, como os empresariais.

    Ao analisar as metodologias descritas acima, considere que:
        As empresas têm a necessidade de procurar e adotar técnicas e abordagens para o processo de desenvolvimento
        de software, a fim de melhorarem as métricas de qualidade, reduzir a taxa de não cumprimento, aumentarem a
        produtividade das equipes e, consequentemente, produzir software com qualidade.
 */

@SpringBootTest (classes = ProdutosApplication.class) //carrega o Context do app em um container Spring Boot, sem um servidor web, mas com JPA e acesso a banco de dados
@ActiveProfiles("test") //indica o profile que o Spring Boot deve utilizar para passar os testes
public class ProdutoServiceIntegracaoTest {

    @Autowired //se carregou o Context do app é possível injetar qualquer Bean do projeto, como Services
    private ProdutoService service;

    @Test //Esta anotação JUnit sinaliza que este método é um caso de teste
    @DisplayName("Busca os produtos na base de dados, espera 5 objetos")
    public void testGetProdutosEsperaUmaPaginaCom5Objetos() { //O nome do método de teste é importante porque deve transmitir a essência do que ele verifica. Este não é um requisito técnico, mas sim uma oportunidade de capturar informações.
        // ARRANGE
        var pageable = PageRequest.of(0, 50);

        // ACT
        var produtos = service.getProdutos(pageable);

        // ASSERT
        assertEquals(5, produtos.getContent().size());
    }

    @Test //Esta anotação JUnit sinaliza que este método é um caso de teste
    public void testGetProdutoByIdEsperaOProdutoDeId1(){
        // ARRANGE + ACT
        var p = service.getProdutoById(1L);

        // ASSERT
        assertNotNull(p);
        assertEquals("Café", p.get().getNome());
        assertEquals("Café em pó tradicional Igaçu lata 400g", p.get().getDescricao());
        assertEquals(new BigDecimal("5.00"), p.get().getValorDeCompra());
        assertEquals(new BigDecimal("10.00"), p.get().getValorDeVenda());
        assertEquals(100, p.get().getEstoque());
        assertEquals(Boolean.TRUE, p.get().getSituacao());

    }

    @Test //Esta anotação JUnit sinaliza que este método é um caso de teste
    public void testGetProdutosByNomeEsperaUmObjetoPorNomePesquisado(){
        // ARRANGE + ACT + ASSERT
        assertEquals(1, service.getProdutosByNome("Café").size());
        assertEquals(1, service.getProdutosByNome("Erva Mate").size());
        assertEquals(1, service.getProdutosByNome("Chá Preto").size());
        assertEquals(1, service.getProdutosByNome("Arroz").size());
        assertEquals(1, service.getProdutosByNome("Feijão").size());
    }

    @Test //Esta anotação JUnit sinaliza que este método é um caso de teste
    public void testInsertEsperaOObjetoInseridoEoDeleta() {
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

        // ASSERT
        assertNotNull(p);
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

    @Test //Esta anotação JUnit sinaliza que este método é um caso de teste
    public void testUpdateEsperaOObjetoAlteradoERetornaAoValorOriginal(){
        // ARRANGE
        var pOriginal = service.getProdutoById(1L).get(); //produto original na base de dados
        var produtoMock = new Produto();
        produtoMock.setId(pOriginal.getId());
        produtoMock.setNome("Teste");
        produtoMock.setDescricao("Desc. do produto Teste");
        produtoMock.setValorDeCompra(new BigDecimal("5.00"));
        produtoMock.setValorDeVenda(new BigDecimal("10.00"));
        produtoMock.setEstoque(100);
        produtoMock.setSituacao(false);

        // ACT
        var produtoAlterado = service.update(produtoMock);

        // ASSERT
        assertNotNull(produtoAlterado);
        assertEquals("Teste", produtoAlterado.getNome());
        assertEquals("Desc. do produto Teste", produtoAlterado.getDescricao());
        assertEquals(new BigDecimal("5.00"), produtoAlterado.getValorDeCompra());
        assertEquals(new BigDecimal("10.00"), produtoAlterado.getValorDeVenda());
        assertEquals(100, produtoAlterado.getEstoque());
        assertEquals(Boolean.FALSE, produtoAlterado.getSituacao());

        //volta ao valor original (para manter a consistência do banco de dados)
        var produtoOriginal = service.update(pOriginal);
        assertNotNull(produtoOriginal);
    }

    @Test //Esta anotação JUnit sinaliza que este método é um caso de teste
    public void testDeleteEsperaAExclusaoDeUmObjetoInserido(){
        // ARRANGE
        var produto = new Produto();
        produto.setNome("Teste");
        produto.setDescricao("Desc. do produto Teste");
        produto.setValorDeCompra(new BigDecimal("5.00"));
        produto.setValorDeVenda(new BigDecimal("10.00"));
        produto.setEstoque(100);
        produto.setSituacao(true);
        var p = service.insert(produto);
        assertNotNull(p);
        //confirma se o produto foi realmente inserido na base de dados
        Long id = p.getId();
        assertNotNull(id);
        p = service.getProdutoById(id).get();
        assertNotNull(p);

        // ACT
        service.delete(id);

        // ASSERT
        if(service.getProdutoById(id).isPresent()){
            fail("O produto não foi excluído");
        }
    }
}
