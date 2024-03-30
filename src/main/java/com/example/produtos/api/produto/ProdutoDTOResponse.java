package com.example.produtos.api.produto;

import java.math.BigDecimal;

public record ProdutoDTOResponse(
    Long id,
    String nome,
    BigDecimal valorDeVenda,
    String descricao,
    Boolean situacao,
    Integer estoque
) {
    public ProdutoDTOResponse(Produto produto){
        this(produto.getId(), produto.getNome(), produto.getValorDeVenda(), produto.getDescricao(), produto.getSituacao(), produto.getEstoque());
    }
}
