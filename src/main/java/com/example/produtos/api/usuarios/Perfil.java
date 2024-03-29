package com.example.produtos.api.usuarios;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity(name = "Perfil")
@Table(name = "perfis")
@Data
public class Perfil implements GrantedAuthority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany(mappedBy = "perfis")
    private List<Usuario> usuario;

    @Override
    public String getAuthority() {
        return nome;
    }
}
