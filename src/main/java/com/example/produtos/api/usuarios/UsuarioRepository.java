package com.example.produtos.api.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Usuario findByUsuario(String usuario);
}
