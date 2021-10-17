package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;

@Repository
public interface JornadaCartaRepository extends JpaRepository<JornadaCarta, Integer> {
    
    String CONSULTA_JORNADA_CARTA_DA_PROXIMA_JOGADA = "select proxima_jornada_carta.* "
                                                    + "from jogada "
                                                    + "join jornada_alternativa on jornada_alternativa.id = jogada.jornada_alternativa_id "
                                                    + "join jornada_carta proxima_jornada_carta on proxima_jornada_carta.id = jornada_alternativa.proxima_jornada_carta_id "
                                                    + "join jornada_carta on jornada_carta.id = jogada.jornada_carta_id "
                                                    + "join jornada on jornada.id = jornada_carta.jornada_id "
                                                    + "where jogada.personagem_id = ?1 "
                                                    + "and jornada.id = ?2 "
                                                    + "order by jogada.id desc "
                                                    + "limit 1" ;

    @Query(value = CONSULTA_JORNADA_CARTA_DA_PROXIMA_JOGADA, nativeQuery = true)
    JornadaCarta buscarProximaJogadaCarta(Integer personagemId, Integer jornadaId);

    String CONSULTA_PRIMEIRA_JOGADA_CARTA   = "select jornada_carta.* "
                                            + "from jornada "
                                            + "join jornada_carta on jornada_carta.jornada_id = jornada.id "
                                            + "where jornada.id = ?1 "
                                            + "and jornada_carta.carta_inicial = 1 "
                                            + "limit 1" ;

    @Query(value = CONSULTA_PRIMEIRA_JOGADA_CARTA, nativeQuery = true)
    JornadaCarta buscarPrimeiraJornadaCarta(Integer jornadaId);
    
}
