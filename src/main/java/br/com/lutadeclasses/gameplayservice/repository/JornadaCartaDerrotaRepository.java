package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.JornadaCartaDerrota;

@Repository
public interface JornadaCartaDerrotaRepository extends JpaRepository<JornadaCartaDerrota, Integer> {
    
    String CONSULTA_CARTA_DERROTA   = "select jornada_carta_derrota.* "
                                    + "from jornada_carta_derrota "
                                    + "join jornada_carta on jornada_carta.id = jornada_carta_derrota.jornada_carta_id "
                                    + "where jornada_carta.jornada_id = ?1 "
                                    + "and jornada_carta_derrota.barra_id = ?2 "
                                    + "limit 1" ;
    @Query(value = CONSULTA_CARTA_DERROTA, nativeQuery = true)
    JornadaCartaDerrota buscarCartaDeDerrota(Integer jornadaId, Integer barraId);
    
}
