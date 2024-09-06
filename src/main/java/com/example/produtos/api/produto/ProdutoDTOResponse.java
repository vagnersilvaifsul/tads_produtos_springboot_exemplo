package com.example.produtos.api.produto;

import java.math.BigDecimal;

/**
 * DTO for {@link Produto}
 */
public record ProdutoDTOResponse(
    Long id,
    String nome,
    BigDecimal valorDeVenda,
    String descricao,
    Integer estoque,
    Boolean situacao
) {
    public ProdutoDTOResponse(Produto produto){
        this(produto.getId(), produto.getNome(), produto.getValorDeVenda(), produto.getDescricao(), produto.getEstoque(), produto.getSituacao());
    }
}
