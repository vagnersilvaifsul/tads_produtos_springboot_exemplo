package com.example.produtos.api.item;

import com.example.produtos.api.pedido.Pedido;
import com.example.produtos.api.produtos.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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
