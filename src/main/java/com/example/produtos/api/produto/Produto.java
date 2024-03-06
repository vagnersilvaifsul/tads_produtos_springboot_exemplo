package com.example.produtos.api.produto;

import com.example.produtos.api.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Collection;

/*
    O Validation vai depender dos requisitos do sistema. A implementação realizada aqui é uma demonstração
    de sua aplicação.
 */

@Entity(name = "Produto")
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotBlank(message = "O nome não pode ser nulo ou vazio") //verifica se está vazio e estabelece como obrigatório (não pode ser nulo)
    @Size(min = 2, max = 50, message = "Tamanho mínimo de 2 e máximo de 200")
    private String nome;
    @NotNull
    @Min(0)
    private BigDecimal valorDeCompra;
    @NotNull
    @Min(0)
    private BigDecimal valorDeVenda;
    @NotBlank(message = "A descrição não pode ser nula ou vazio") //verifica se está vazio e estabelece como obrigatório (não pode ser nulo)
    @Size(min = 2, max = 255, message = "Tamanho mínimo de 2 e máximo de 255")
    private String descricao;
    @NotNull
    private Boolean situacao;
    @NotNull
    @Min(0)
    private Integer estoque;
    @OneToMany(mappedBy = "produto")
    private Collection<Item> items;

    public static Produto create(ProdutoDTO p){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(p, Produto.class);
    }
}
