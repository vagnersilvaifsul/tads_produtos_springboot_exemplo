package com.example.produtos.api.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface TokenConfirmacaoEmailRepository extends JpaRepository<TokenConfirmacaoEmail, Long> {
    TokenConfirmacaoEmail findByToken(String token);
}