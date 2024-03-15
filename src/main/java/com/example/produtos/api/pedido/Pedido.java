package com.example.produtos.api.pedido;


import com.example.produtos.api.cliente.Cliente;
import com.example.produtos.api.item.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

@Entity(name = "Pedido")
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pagamento;
    private String estado;
    private Date dataCriacao;
    private Date dataModificacao;
    private Byte situacao;
    private BigDecimal totalPedido;
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER)
    private Collection<Item> items;
    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false)
    private Cliente cliente;
}
