package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;

@Repository
public interface JornadaCartaRepository extends JpaRepository<JornadaCarta, Integer> {

    String CONSULTA_PRIMEIRA_JOGADA_CARTA   = "select jornada_carta.* "
                                            + "from jornada "
                                            + "join jornada_carta on jornada_carta.jornada_id = jornada.id "
                                            + "where jornada.id = ?1 "
                                            + "and jornada_carta.posicao = 'INICIO' "
                                            + "limit 1" ;
    @Query(value = CONSULTA_PRIMEIRA_JOGADA_CARTA, nativeQuery = true)
    JornadaCarta buscarPrimeiraJornadaCarta(Integer jornadaId);
    
}
