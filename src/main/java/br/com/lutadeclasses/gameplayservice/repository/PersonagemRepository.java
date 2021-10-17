package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.Personagem;

@Repository
public interface PersonagemRepository extends JpaRepository<Personagem, Integer> {
    
}
