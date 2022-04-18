package com.example.produtos.api.produtos;

import lombok.Data;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Data
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private Long estoque;
    private Boolean situacao;

    public static ProdutoDTO create(Produto p){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(p, ProdutoDTO.class);
    }
}
