package com.example.produtos.api.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenConfirmacaoEmailRepository extends JpaRepository<TokenConfirmacaoEmail, Long> {
    TokenConfirmacaoEmail findByToken(String token);
}