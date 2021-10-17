package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;

@Repository
public interface PersonagemBarraRepository extends JpaRepository<PersonagemBarra, Integer> {
    
}
