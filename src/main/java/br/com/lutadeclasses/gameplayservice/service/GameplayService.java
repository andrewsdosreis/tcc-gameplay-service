package br.com.lutadeclasses.gameplayservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;
import br.com.lutadeclasses.gameplayservice.exception.GameOverException;
import br.com.lutadeclasses.gameplayservice.exception.JogadaJaExisteException;
import br.com.lutadeclasses.gameplayservice.exception.JornadaAlternativaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.PersonagemNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.model.request.JogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.AlternativaDto;
import br.com.lutadeclasses.gameplayservice.model.response.ProximaJogadaDto;

@Service
public class GameplayService {

    private JornadaService jornadaService;
    private PersonagemService personagemService;
    private JogadaService jogadaService;
    private ObjectMapper objectMapper;

    public GameplayService(JornadaService jornadaService, PersonagemService personagemService, JogadaService jogadaService, ObjectMapper objectMapper) {
        this.jornadaService = jornadaService;
        this.personagemService = personagemService;
        this.jogadaService = jogadaService;
        this.objectMapper = objectMapper;
    }

    public ProximaJogadaDto buscarProximaJogada(Integer personagemId, Integer jornadaId) {
        Optional<JornadaCarta> jornadaCarta = jornadaService.buscarProximaJornadaCarta(personagemId, jornadaId);
        if (jornadaCarta.isEmpty()) {
            Optional<Jogada> jogada = jogadaService.buscarPrimeiraJogadaDoPersonagemNaJornada(personagemId, jornadaId);
            if (jogada.isEmpty()) {
                jornadaCarta = jornadaService.buscarPrimeiraCartaDaJornada(jornadaId);
            }
        }

        return montarProximaJogada(personagemId, jornadaId, jornadaCarta.orElseThrow(() -> new JornadaCartaNaoEncontradaException(personagemId, jornadaId)));
    }

    public ProximaJogadaDto fazerJogada(JogadaDto jogadaDto) {
        //todo: add validacao -> se sessao esta aberta

        Optional<Jogada> jogada = jogadaService.buscarJogada(jogadaDto.getPersonagemId(), jogadaDto.getJornadaCartaId());
    
        if (jogada.isPresent()) {
            throw new JogadaJaExisteException(jogadaDto.getPersonagemId(), jogadaDto.getJornadaId(), jogadaDto.getJornadaCartaId());
        }

        Personagem personagem = buscarPersonagem(jogadaDto.getPersonagemId());
        JornadaCarta jornadaCarta = buscarJornadaCarta(jogadaDto.getJornadaCartaId());
        JornadaAlternativa jornadaAlternativa = buscarJornadaAlternativa(jornadaCarta, jogadaDto.getAlternativaEscolhidaId());
        jogadaService.inserirJogada(personagem, jornadaCarta, jornadaAlternativa);
        personagemService.atualizarPersonagemBarra(personagem.getPersonagemBarraList(), jornadaAlternativa.getAlternativa().getAcaoList());

        if (verificarSeJogoAcabou(personagem.getPersonagemBarraList())) {
            throw new GameOverException();
        }
        
        return montarProximaJogada(jogadaDto.getPersonagemId(), jogadaDto.getJornadaId(), jornadaAlternativa.getProximaJornadaCarta());
    }

    private Personagem buscarPersonagem(Integer personagemId) {
        return personagemService.buscarPersonagem(personagemId).orElseThrow(() -> new PersonagemNaoEncontradoException(personagemId));
    }

    private JornadaCarta buscarJornadaCarta(Integer jornadaCartaId) {
        return jornadaService.buscarJornadaCarta(jornadaCartaId).orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaCartaId));
    }

    private JornadaAlternativa buscarJornadaAlternativa(JornadaCarta jornadaCarta, Integer alternativaId) {
        return jornadaCarta
                    .getJornadaAlternativaList()
                    .stream()
                    .filter(obj -> obj.getAlternativa().getId().equals(alternativaId))
                    .findFirst()
                    .orElseThrow(() -> new JornadaAlternativaNaoEncontradaException(jornadaCarta.getId(), alternativaId));
    }

    private ProximaJogadaDto montarProximaJogada(Integer personagemId, Integer jornadaId, JornadaCarta jornadaCarta) {
        return ProximaJogadaDto
                .builder()
                .personagemId(personagemId)
                .jornadaId(jornadaId)
                .jornadaCartaId(jornadaCarta.getId())
                .cartaId(jornadaCarta.getCarta().getId())
                .cartaDescricao(jornadaCarta.getCarta().getDescricao())
                .alternativas(jornadaCarta
                                .getCarta()
                                .getAlternativas()
                                .stream()
                                .map(alternativa -> objectMapper.convertValue(alternativa, AlternativaDto.class))
                                .collect(Collectors.toList()))
                .build();
    }

    private boolean verificarSeJogoAcabou(List<PersonagemBarra> personagemBarraList) {
        return personagemBarraList.stream().anyMatch(barra -> barra.getValor() <= 0);
    }

}
