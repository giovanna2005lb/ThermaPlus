package br.com.fiap.ThermaPlus.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.ThermaPlus.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}

