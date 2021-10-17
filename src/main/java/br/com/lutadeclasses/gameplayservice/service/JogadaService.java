package br.com.lutadeclasses.gameplayservice.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.repository.JogadaRepository;

@Service
public class JogadaService {

    private JogadaRepository jogadaRepository;

    public JogadaService(JogadaRepository jogadaRepository) {
        this.jogadaRepository = jogadaRepository;
    }

    public Optional<Jogada> buscarJogada(Integer personagemId, Integer jornadaCartaId) {
        return Optional.ofNullable(jogadaRepository.buscarJogada(personagemId, jornadaCartaId));
    }

    public Optional<Jogada> buscarPrimeiraJogadaDoPersonagemNaJornada(Integer personagemId, Integer jornadaId) {
        return Optional.ofNullable(jogadaRepository.buscarPrimeiraJogadaDoPersonagemNaJornada(personagemId, jornadaId));
    }
    
    public void inserirJogada(Personagem personagem, JornadaCarta jornadaCarta, JornadaAlternativa jornadaAlternativa) {
        jogadaRepository.save(new Jogada(obj -> {
            obj.setJornadaAlternativa(jornadaAlternativa);
            obj.setJornadaCarta(jornadaCarta);
            obj.setPersonagem(personagem);
            obj.setDataHora(LocalDateTime.now());
        }));
    }

}
