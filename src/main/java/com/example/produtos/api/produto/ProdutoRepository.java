package com.example.produtos.api.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
  A grande vantagem do Padrão Repository reside no fato de ele permitir montar consultas pelo padrão de nome do método
  (e de trazer o CRUD pronto, sem precisar escrever uma linha de código).
  Note que ele utiliza o padrão Domain Speak na busca dos dados (e deixa para o JPA aplicar o padrão Query Speak).
 */
public interface ProdutoRepository extends JpaRepository<Produto,Long> {
    /*
        O Padrão Repository do Spring Data, apesar de nos fornecer uma inferface Domain Speak, inteligente o
        suficiente para montar o SQL da consulta para nós, é flexível o bastante para nos permitir utilizar as
        nossas próprias Query Speak (é o caso do SQL apresentado na anotação @Query). Ou seja, o Spring Data nos
        permite alternar entre Domain Speak e Query Speak rapidamente.
     */
    //Exemplo de aplicação de Query Speak no Padrão Repository
    @Query(value = "SELECT p FROM Produto p where p.nome like ?1 order by p.nome")
    List<Produto> findByNome(String nome);

    //A mesma consulta poderia ter sido resolvida com o Padrão Repository em Domain Speak
    //List<Produto> findByNomeBeforeOrderByNome(String nome);
}
