package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;

@Repository
public interface JornadaAlternativaRepository extends JpaRepository<JornadaAlternativa, Integer> {

    @Query(value = "select * from jornada_alternativa where jornada_carta_id = ?1 and alternativa_id = ?2", nativeQuery = true)
    JornadaAlternativa buscarJornadaAlternativa(Integer jornadaCartaId, Integer alternativaId);

}
