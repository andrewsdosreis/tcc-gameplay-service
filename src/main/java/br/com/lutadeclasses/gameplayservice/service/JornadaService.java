package br.com.lutadeclasses.gameplayservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.repository.JornadaCartaRepository;

@Service
public class JornadaService {
    
    private JornadaCartaRepository jornadaCartaRepository;

    public JornadaService(JornadaCartaRepository jornadaCartaRepository) {
        this.jornadaCartaRepository = jornadaCartaRepository;
    }

    public Optional<JornadaCarta> buscarPrimeiraCartaDaJornada(Integer jornadaId) {
        return Optional.ofNullable(jornadaCartaRepository.buscarPrimeiraJornadaCarta(jornadaId));
    }

    public Optional<JornadaCarta> buscarJornadaCarta(Integer jornadaCartaId) {
        return Optional.ofNullable(jornadaCartaRepository.buscarJornadaCartaPorId(jornadaCartaId));
    }

    public Optional<JornadaCarta> buscarProximaJornadaCarta(Integer personagemId, Integer jornadaId) {
        return Optional.ofNullable(jornadaCartaRepository.buscarProximaJogadaCarta(personagemId, jornadaId));
    }

}
