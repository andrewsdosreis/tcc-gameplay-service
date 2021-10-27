package br.com.lutadeclasses.gameplayservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.Sessao;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
    
    Optional<Sessao> findFirstByTitulo(String titulo);
    
}
