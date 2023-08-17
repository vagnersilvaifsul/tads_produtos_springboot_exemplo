package com.example.produtos.api.produtos;

import com.example.produtos.api.item.Item;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity(name = "produtos")
@Data
public class Produto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String nome;
    private BigDecimal valorDeCompra;
    private BigDecimal valorDeVenda;
    private String descricao;
    private Byte situacao;
    private Integer quantidade;
    @OneToMany(mappedBy = "produto")
    private Collection<Item> items;

    public static Produto create(ProdutoDTO p){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(p, Produto.class);
    }
}
