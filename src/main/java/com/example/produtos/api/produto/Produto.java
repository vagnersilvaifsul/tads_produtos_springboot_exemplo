package com.example.produtos.api.produto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

/*
    O Validation vai depender dos requisitos do sistema. A implementação realizada aqui é uma demonstração
    de sua aplicação.
 */

@Entity(name = "Produto")
@Table(name = "produtos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal valorDeCompra;
    private BigDecimal valorDeVenda;
    private String descricao;
    private Boolean situacao;
    private Integer estoque;

    public Produto(String nome, BigDecimal valorDeCompra, BigDecimal valorDeVenda, String descricao, Integer estoque) {
        this.nome = nome;
        this.valorDeCompra = valorDeCompra;
        this.valorDeVenda = valorDeVenda;
        this.descricao = descricao;
        this.situacao = true;
        this.estoque = estoque;
    }

    public static Produto create(ProdutoDTOResponse p) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(p, Produto.class);
    }
}
