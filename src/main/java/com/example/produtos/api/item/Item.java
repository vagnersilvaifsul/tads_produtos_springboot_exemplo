package com.example.produtos.api.item;

import com.example.produtos.api.pedido.Pedido;
import com.example.produtos.api.produto.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name="Item")
@Table(name = "itens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal quantidade;
    private BigDecimal totalItem;
    private Byte situacao;
    @ManyToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id", nullable = false)
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "id", nullable = false)
    private Produto produto;
}
