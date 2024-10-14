package com.example.produtos.api.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
  A grande vantagem do Padrão Repository reside no fato de ele permitir montar consultas pelo padrão de nome do método
  (e, claro, de trazer o CRUD pronto, sem precisar escrever uma linha de código).
  Um repositório reúne, essencialmente, todas as operações de dados para um determinado tipo de domínio em um só lugar.
  O aplicativo se comunica com o repositório em Domain Speak, e o repositório, por sua vez, se comunica com o armazenamento
  de dados em Query Speak.
 */
/*
    Esta classe implementa o CRUD de usuários. A autenticação fica a cargo da AutenticacaoRepository.
 */
@RepositoryRestResource(exported = false)
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Boolean existsByEmail(String email);

    Usuario findByEmail(String email);
}
