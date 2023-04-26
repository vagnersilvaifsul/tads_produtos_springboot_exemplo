package com.example.produtos.api.produtos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {

    @Query(value = "SELECT p FROM Produto p where p.nome like ?1")
    List<Produto> findByNome(String nome);
}
