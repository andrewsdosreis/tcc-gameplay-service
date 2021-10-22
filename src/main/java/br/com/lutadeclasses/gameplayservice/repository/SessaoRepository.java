package br.com.lutadeclasses.gameplayservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lutadeclasses.gameplayservice.entity.Sessao;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
    @Query(value = "select * from sessao where id = ?1", nativeQuery = true)
    Sessao buscarSessao(Integer sessaoId);
}
