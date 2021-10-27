package br.com.lutadeclasses.gameplayservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.Jornada;
import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCartaDerrota;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaAlternativaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaCartaDerrotadaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.model.PosicaoCartaEnum;
import br.com.lutadeclasses.gameplayservice.repository.JornadaCartaDerrotaRepository;
import br.com.lutadeclasses.gameplayservice.repository.JornadaCartaRepository;
import br.com.lutadeclasses.gameplayservice.repository.JornadaRepository;

@Service
public class JornadaService {
    
    private static final List<String> POSICAO_FIM_DE_JOGO = Arrays.asList(PosicaoCartaEnum.DERROTA.toString(), PosicaoCartaEnum.VITORIA.toString());

    private JornadaRepository jornadaRepository;
    private JornadaCartaRepository jornadaCartaRepository;
    private JornadaCartaDerrotaRepository jornadaCartaDerrotaRepository;

    public JornadaService(JornadaRepository jornadaRepository, JornadaCartaRepository jornadaCartaRepository, JornadaCartaDerrotaRepository jornadaCartaDerrotaRepository) {
        this.jornadaRepository = jornadaRepository;
        this.jornadaCartaRepository = jornadaCartaRepository;
        this.jornadaCartaDerrotaRepository = jornadaCartaDerrotaRepository;
    }

    public Jornada buscarJornada(Integer jornadaId) {
        return jornadaRepository.findById(jornadaId)
                                .orElseThrow(() -> new JornadaNaoEncontradaException(jornadaId));
    }

    public JornadaCarta buscarJornadaCarta(Integer jornadaCartaId) {
        return jornadaCartaRepository.findById(jornadaCartaId)
                                     .orElseThrow(() -> new JornadaCartaNaoEncontradaException(jornadaCartaId));
    }

    public JornadaAlternativa buscarJornadaAlternativa(JornadaCarta jornadaCarta, Integer jornadaAlternativaId) {
        return jornadaCarta
                    .getJornadaAlternativaList()
                    .stream()
                    .filter(obj -> obj.getId().equals(jornadaAlternativaId))
                    .findFirst()
                    .orElseThrow(() -> new JornadaAlternativaNaoEncontradaException(jornadaCarta.getId(), jornadaAlternativaId));
    }

    public JornadaCartaDerrota buscarJornadaCartaDeDerrota(Integer jornadaId, Integer barraId) {
        return Optional.ofNullable(jornadaCartaDerrotaRepository.buscarCartaDeDerrota(jornadaId, barraId))
                       .orElseThrow(() -> new JornadaCartaDerrotadaNaoEncontradaException(jornadaId, barraId));
    }

    public Optional<JornadaCarta> buscarPrimeiraCartaDaJornada(Integer jornadaId) {
        return Optional.ofNullable(jornadaCartaRepository.buscarPrimeiraJornadaCarta(jornadaId));
    }

    public boolean verificarSeJornadaChegouAoFim(JornadaCarta jornadaCarta) {
        return POSICAO_FIM_DE_JOGO.contains(jornadaCarta.getPosicao());
    }

    public boolean verificarSeJornadaChegouAoFimComVitoria(JornadaCarta jornadaCarta) {
        return jornadaCarta.getPosicao().equals(PosicaoCartaEnum.VITORIA.toString());
    }

}
