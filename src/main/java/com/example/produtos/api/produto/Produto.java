package com.example.produtos.api.produto;

import com.example.produtos.api.item.Item;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collection;

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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal valorDeCompra;
    private BigDecimal valorDeVenda;
    private String descricao;
    private Boolean situacao;
    private Integer estoque;
    @OneToMany(mappedBy = "produto", fetch = FetchType.EAGER)
    private Collection<Item> items;

    public static Produto create(ProdutoDTOResponse p){
        var modelMapper = new ModelMapper();
        return modelMapper.map(p, Produto.class);
    }
}
