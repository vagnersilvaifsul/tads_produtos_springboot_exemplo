package com.example.produtos.api.pedido;


import com.example.produtos.api.cliente.Cliente;
import com.example.produtos.api.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String pagamento;
    private String estado;
    private Date dataCriacao;
    private Date dataModificacao;
    private Byte situacao;
    private BigDecimal totalPedido;
    @OneToMany(mappedBy = "pedido")
    private Collection<Item> items;
    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false)
    private Cliente cliente;
}
