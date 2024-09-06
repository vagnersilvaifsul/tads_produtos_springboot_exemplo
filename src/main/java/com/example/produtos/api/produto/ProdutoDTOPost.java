package com.example.produtos.api.produto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO for {@link Produto}
 */
public record ProdutoDTOPost(
    @NotBlank(message = "O nome não pode ser nulo ou vazio") //verifica se está vazio e estabelece como obrigatório (não pode ser nulo)
    @Size(min = 2, max = 50, message = "Tamanho mínimo de 2 e máximo de 200")
    String nome,
    @NotBlank(message = "A descrição não pode ser nula ou vazio") //verifica se está vazio e estabelece como obrigatório (não pode ser nulo)
    @Size(min = 2, max = 255, message = "Tamanho mínimo de 2 e máximo de 255")
    String descricao,
    @NotNull
    @Min(0)
    BigDecimal valorDeCompra,
    @NotNull
    @Min(0)
    BigDecimal valorDeVenda,
    @NotNull
    @Min(0)
    Integer estoque
) {
    public ProdutoDTOPost(Produto produto){
        this(produto.getNome(), produto.getDescricao(), produto.getValorDeCompra(), produto.getValorDeVenda(), produto.getEstoque());
    }
}
