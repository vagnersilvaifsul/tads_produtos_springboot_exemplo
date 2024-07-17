package com.example.produtos.api.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity(name = "Perfil")
@Table(name = "perfis")
@NoArgsConstructor
@Getter
@Setter
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
