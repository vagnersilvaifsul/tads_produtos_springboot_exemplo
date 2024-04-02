package com.example.produtos.api.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
  A grande vantagem do Padrão Repository reside no fato de ele permitir montar consultas pelo padrão de nome do método
  (e, claro, de trazer o CRUD pronto, sem precisar escrever uma linha de código).
  Um repositório reúne, essencialmente, todas as operações de dados para um determinado tipo de domínio em um só lugar.
  O aplicativo se comunica com o repositório em Domain Speak, e o repositório, por sua vez, se comunica com o armazenamento
  de dados em Query Speak.
 */

/*
    JpaRepository tem como superclasse raiz a interface Repository, uma interface de marcação, sem nada dentro.
    Repository serve para indicar ao Spring Boot que a classe que a extende deve ser inserida no Context do
    aplicativo pelo Spring Boot (é por isso que não precisa inserir uma anotação acima da classe).
 */
public interface ProdutoRepository extends JpaRepository<Produto,Long> {
    /*
        O Padrão Repository do Spring Data, apesar de nos fornecer uma inferface Domain Speak, inteligente o
        suficiente para montar o SQL da consulta para nós, é flexível o bastante para nos permitir utilizar as
        nossas próprias Query Speak (é o caso do SQL apresentado na anotação @Query). Ou seja, o Spring Data nos
        permite alternar entre Domain Speak e Query Speak rapidamente. É muita flexibilidade!
     */
    //Exemplo de aplicação de Query Speak no Padrão Repository
    @Query(value = "SELECT p FROM Produto p where p.nome like ?1 order by p.nome")
    List<Produto> findByNome(String nome);

    //A mesma consulta poderia ter sido resolvida com o Padrão Repository em Domain Speak
    //List<Produto> findByNomeStartsWithOrderByNome(String nome);

    /*
        O Spring Data é tão flexível que é possível utlizar SQL no lugar do JPQL, basta adicionar o argumento
        nativeQuery=true na @Query
        Se fossermos reescrever o exemplo de findByNome em SQL, ficaria assim:
        @Query(value = "SELECT p FROM produtos p where p.nome like ?1 order by p.nome", nativeQuery=true)
     */
}
