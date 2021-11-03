package br.com.lutadeclasses.gameplayservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.Acao;
import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.factory.ProximaJogadaFactory;
import br.com.lutadeclasses.gameplayservice.model.PersonagemStatusEnum;
import br.com.lutadeclasses.gameplayservice.model.request.JogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.ProximaJogadaDto;

@Service
public class GameplayService {

    private JornadaService jornadaService;
    private PersonagemService personagemService;
    private JogadaService jogadaService;
    private SessaoService sessaoService;

    public GameplayService(JornadaService jornadaService, PersonagemService personagemService, JogadaService jogadaService, SessaoService sessaoService) {
        this.jornadaService = jornadaService;
        this.personagemService = personagemService;
        this.jogadaService = jogadaService;
        this.sessaoService = sessaoService;
    }

    public ProximaJogadaDto buscarProximaJogada(Integer personagemId, Integer jornadaId) {
        Optional<JornadaCarta> proximaJornadaCarta;
        
        Personagem personagem = personagemService.buscarPersonagem(personagemId);
        Optional<Jogada> ultimaJogada = jogadaService.buscarUltimaJogadaDoPersonagemNaJornada(personagemId, jornadaId);
        
        if (ultimaJogada.isPresent()) {
            proximaJornadaCarta = Optional.of(buscarProximaJornadaCarta(ultimaJogada.get()));
        } else {
            proximaJornadaCarta = jornadaService.buscarPrimeiraCartaDaJornada(jornadaId);
        }

        return ProximaJogadaFactory.build(personagem, proximaJornadaCarta.orElseThrow(() -> new JornadaCartaNaoEncontradaException(personagemId, jornadaId)));
    }

    public ProximaJogadaDto fazerJogada(JogadaDto jogadaDto) {
        var personagem = personagemService.buscarPersonagem(jogadaDto.getPersonagemId());
        
        validarJogada(jogadaDto, personagem);

        var jornadaCarta = jornadaService.buscarJornadaCarta(jogadaDto.getJornadaCartaId());
        var jornadaAlternativa = jornadaService.buscarJornadaAlternativa(jornadaCarta, jogadaDto.getJornadaAlternativaId());        
        var jogada = jogadaService.inserirJogada(personagem, jornadaCarta, jornadaAlternativa);

        atualizarPersonagemAposCadaJogada(personagem, jornadaAlternativa.getAlternativa().getAcoes());

        if (personagemService.validarSePersonagemFoiDerrotado(personagem)) {
            var barraQueDerrotouPersonagem = personagemService.buscarBarraQueCausouDerrotaDoPersonagem(personagem);
            var jornadaCartaDaDerrota = jornadaService.buscarJornadaCartaDeDerrota(jogadaDto.getJornadaId(), barraQueDerrotouPersonagem.getBarra().getId());
            jogada = inserirFimDeJogo(personagem, jornadaCartaDaDerrota.getJornadaCarta(), PersonagemStatusEnum.DERROTADO);
            return ProximaJogadaFactory.build(personagem, buscarProximaJornadaCarta(jogada));
        }

        if (jornadaService.verificarSeJornadaChegouAoFimComVitoria(jornadaAlternativa.getProximaJornadaCarta())) {
            jogada = inserirFimDeJogo(personagem, jornadaAlternativa.getProximaJornadaCarta(), PersonagemStatusEnum.VENCEDOR);
        }

        return ProximaJogadaFactory.build(personagem, buscarProximaJornadaCarta(jogada));
    }

    private void validarJogada(JogadaDto jogadaDto, Personagem personagem) {
        jogadaService.validarSeJogadaJaFoiExecutada(jogadaDto.getPersonagemId(), jogadaDto.getJornadaId(), jogadaDto.getJornadaCartaId());
        sessaoService.validarSeSessaoEstaAberta(personagem.getSessao());
        personagemService.validarSePersonagemEstaNaJornada(personagem, jogadaDto.getJornadaId());
        personagemService.validarSePersonagemAindaEstaJogando(personagem);
    }

    private void atualizarPersonagemAposCadaJogada(Personagem personagem, List<Acao> acoes) {
        if (personagem.getStatus().equals(PersonagemStatusEnum.REGISTRADO.toString())) {
            personagemService.atualizarStatusDoPersonagem(personagem, PersonagemStatusEnum.JOGANDO);
        }
        personagemService.atualizarPersonagemBarra(personagem.getPersonagemBarras(), acoes);
    }

    private Jogada inserirFimDeJogo(Personagem personagem, JornadaCarta jornadaCarta, PersonagemStatusEnum status) {
        personagemService.atualizarStatusDoPersonagem(personagem, status);
        return jogadaService.inserirJogada(personagem, jornadaCarta, null);
    }

    private JornadaCarta buscarProximaJornadaCarta(Jogada jogada) {
        return jornadaService.verificarSeJornadaChegouAoFim(jogada.getJornadaCarta()) ?
                jogada.getJornadaCarta() : jogada.getJornadaAlternativa().getProximaJornadaCarta();
    }

}
