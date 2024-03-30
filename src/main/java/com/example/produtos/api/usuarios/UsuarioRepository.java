package com.example.produtos.api.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

/*
  A grande vantagem do Padrão Repository reside no fato de ele permitir montar consultas pelo padrão de nome do método
  (e de trazer o CRUD pronto, sem precisar escrever uma linha de código).
  Note que ele utiliza o padrão Domain Speak na busca dos dados (e deixa para o JPA aplicar o padrão Query Speak).
 */
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Usuario findByUsuario(String usuario);
}
