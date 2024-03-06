package com.example.produtos.api.cliente;

import com.example.produtos.api.pedido.Pedido;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity(name="Cliente")
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String nome;
    private String sobrenome;
    private Byte situacao;
    @OneToMany(mappedBy = "cliente")
    private Collection<Pedido> pedidos;
}
