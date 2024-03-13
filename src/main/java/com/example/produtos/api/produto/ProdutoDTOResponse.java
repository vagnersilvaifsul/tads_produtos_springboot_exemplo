package com.example.produtos.api.produto;

import lombok.Data;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Data
public class ProdutoDTOResponse {
    private Long id;
    private String nome;
    private BigDecimal valorDeVenda;
    private String descricao;
    private Boolean situacao;
    private Integer estoque;

    public static ProdutoDTOResponse create(Produto p){
        var modelMapper = new ModelMapper();
        return modelMapper.map(p, ProdutoDTOResponse.class);
    }
}
