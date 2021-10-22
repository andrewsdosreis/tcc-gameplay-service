package br.com.lutadeclasses.gameplayservice.service;

import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.exception.JornadaAlternativaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.PersonagemNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.model.PosicaoCartaEnum;
import br.com.lutadeclasses.gameplayservice.model.request.JogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.AlternativaDto;
import br.com.lutadeclasses.gameplayservice.model.response.ProximaJogadaDto;

@Service
public class GameplayService {

    private JornadaService jornadaService;
    private PersonagemService personagemService;
    private JogadaService jogadaService;
    private SessaoService sessaoService;
    private ObjectMapper objectMapper;

    public GameplayService(JornadaService jornadaService, PersonagemService personagemService, JogadaService jogadaService, SessaoService sessaoService, ObjectMapper objectMapper) {
        this.jornadaService = jornadaService;
        this.personagemService = personagemService;
        this.jogadaService = jogadaService;
        this.sessaoService = sessaoService;
        this.objectMapper = objectMapper;
    }

    public ProximaJogadaDto buscarProximaJogada(Integer personagemId, Integer jornadaId) {
        Optional<JornadaCarta> jornadaCarta = jornadaService.buscarProximaJornadaCarta(personagemId, jornadaId);
        if (jornadaCarta.isEmpty() &&
            jogadaService.buscarPrimeiraJogadaDoPersonagemNaJornada(personagemId, jornadaId).isEmpty()) {
                jornadaCarta = jornadaService.buscarPrimeiraCartaDaJornada(jornadaId);
        }

        return montarProximaJogada(personagemId, jornadaId, jornadaCarta.orElseThrow(() -> new JornadaCartaNaoEncontradaException(personagemId, jornadaId)));
    }

    public ProximaJogadaDto fazerJogada(JogadaDto jogadaDto) {
        jogadaService.validarSeJogadaJaFoiExecutada(jogadaDto.getPersonagemId(), jogadaDto.getJornadaId(), jogadaDto.getJornadaCartaId());
        
        Personagem personagem = buscarPersonagem(jogadaDto.getPersonagemId());
        sessaoService.validarSeSessaoEstaAberta(personagem.getSessao());
        
        JornadaCarta jornadaCarta = buscarJornadaCarta(jogadaDto.getJornadaCartaId());
        JornadaAlternativa jornadaAlternativa = buscarJornadaAlternativa(jornadaCarta, jogadaDto.getAlternativaEscolhidaId());
        
        jogadaService.inserirJogada(personagem, jornadaCarta, jornadaAlternativa);
        
        personagemService.atualizarPersonagemBarra(personagem.getPersonagemBarraList(), jornadaAlternativa.getAlternativa().getAcaoList());        
        personagemService.verificarSePersonagemFoiDerrotado(personagem);

        if (jornadaAlternativa.getProximaJornadaCarta().getPosicao().equals(PosicaoCartaEnum.FIM.toString())) {
            jogadaService.inserirJogada(personagem, jornadaAlternativa.getProximaJornadaCarta(), null);
            personagemService.atualizarPersonagemParaVencedor(personagem);
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
        String nomePersonagem = buscarPersonagem(personagemId).getNome();
        return ProximaJogadaDto
                .builder()
                .personagemId(personagemId)
                .jornadaId(jornadaId)
                .jornadaCartaId(jornadaCarta.getId())
                .cartaId(jornadaCarta.getCarta().getId())
                .cartaDescricao(ajustaDescricaoDaCarta(jornadaCarta.getCarta().getDescricao(), nomePersonagem))
                .alternativas(jornadaCarta
                                .getCarta()
                                .getAlternativas()
                                .stream()
                                .map(alternativa -> objectMapper.convertValue(alternativa, AlternativaDto.class))
                                .collect(Collectors.toList()))
                .build();
    }

    private String ajustaDescricaoDaCarta(String cartaDescricao, String nomeJogador) {
        return cartaDescricao.replace("%JOGADOR%", nomeJogador);
    }
    
}
