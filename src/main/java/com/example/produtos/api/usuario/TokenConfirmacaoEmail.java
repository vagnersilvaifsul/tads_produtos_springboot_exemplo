package com.example.produtos.api.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="token_confirmacao_email")
@Getter
@Setter
@NoArgsConstructor
public class TokenConfirmacaoEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataDeCriacao;

    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public TokenConfirmacaoEmail(Usuario usuario) {
        this.usuario = usuario;
        dataDeCriacao = LocalDateTime.now();
        token = UUID.randomUUID().toString();
    }

}
