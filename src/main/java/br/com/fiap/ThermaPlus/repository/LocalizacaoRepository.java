package br.com.fiap.ThermaPlus.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.ThermaPlus.model.Localizacao;
import br.com.fiap.ThermaPlus.model.Usuario;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

    Page<Localizacao> findByUsuario(Usuario usuario, Pageable pageable);

    List<Localizacao> findByUsuario(Usuario usuario);
}



