package com.example.produtos.api.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

/*
  A grande vantagem do Padrão Repository reside no fato de ele permitir montar consultas pelo padrão de nome do método
  (e, claro, de trazer o CRUD pronto, sem precisar escrever uma linha de código).
  Um repositório reúne, essencialmente, todas as operações de dados para um determinado tipo de domínio em um só lugar.
  O aplicativo se comunica com o repositório em Domain Speak, e o repositório, por sua vez, se comunica com o armazenamento
  de dados em Query Speak.
 */
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Usuario findByUsuario(String usuario);
}
