package com.example.produtos.api.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/*
        O Padrão Repository do Spring Data, apesar de nos fornecer uma inferface Domain Speak, inteligente o
        suficiente para montar o SQL da consulta para nós, é flexível o bastante para nos permitir utilizar as
        nossas próprias Query Speak (é o caso do SQL apresentado na anotação @Query). Ou seja, o Spring Data nos
        permite alternar entre Domain Speak e Query Speak rapidamente. É muita flexibilidade!
 */

@RepositoryRestResource(exported = false)
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    /*
        Para exemplificar eu coloquei a mesma busca em Domain Speak e Query Speak, só variei os nomes dos métodos.
        Assim vocês podem contrastar as três diferentes maneiras de buscar dados no banco de dados com Spring Data.
     */

    //Método em Domain Speak (só pelo nome do método o Spring Data irá injetar o SQL em tempo de execução)
    Optional<List<Produto>> findByNome(String nome);

    //Método em Query Speak (em JPQL). Note o nome da entidade Produto no JPQL, isso mostra que ele roda no objeto JPA.
    @Query("SELECT p FROM Produto p WHERE p.nome LIKE :nome%")
    Optional<List<Produto>> findByNomeQuerySpeakJPQL(String nome);

    //Método em Query Speak (em SQL). Note que utiliza o tradicional SQl, inclusive o nome da tabela é a que está no banco de dados.
    @Query(value = "SELECT p.* FROM produtos p WHERE p.nome LIKE CONCAT(?1, '%') ORDER BY p.nome", nativeQuery = true)
    Optional<List<Produto>> findByNomeQuerySpeakSQL(String nome);
}
