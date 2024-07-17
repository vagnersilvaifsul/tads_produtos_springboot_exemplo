package com.example.produtos.api.pedido;


import com.example.produtos.api.cliente.Cliente;
import com.example.produtos.api.item.Item;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

@Entity(name = "Pedido")
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
