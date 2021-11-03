package br.com.lutadeclasses.gameplayservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;

@Repository
public interface JogadaRepository extends JpaRepository<Jogada, Integer> {
    
    @Query(value = "select * from jogada where personagem_id = ?1 and jornada_carta_id = ?2", nativeQuery = true)
    Jogada buscarJogada(Integer personagemId, Integer jornadaCartaId);
    
    String CONSULTA_ULTIMA_JOGADA_DO_PERSONAGEM = "select jogada.* "
                                                + "from jogada "
                                                + "join jornada_carta on jornada_carta.id = jogada.jornada_carta_id "
                                                + "where jogada.personagem_id = ?1 "
                                                + "and jornada_carta.jornada_id = ?2 "
                                                + "order by jogada.id desc "
                                                + "limit 1" ;
    @Query(value = CONSULTA_ULTIMA_JOGADA_DO_PERSONAGEM, nativeQuery = true)
    Jogada buscarUltimaJogadaDoPersonagemNaJornada(Integer personagemId, Integer jornadaId);

    List<Jogada> findByPersonagem(Personagem personagem);
}
