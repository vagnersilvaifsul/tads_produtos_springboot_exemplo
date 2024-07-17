package com.example.produtos.api.cliente;

import com.example.produtos.api.pedido.Pedido;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity(name="Cliente")
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    private Byte situacao;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private Collection<Pedido> pedidos;
}
