package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.Alternativa;

@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Integer> {
    
}
