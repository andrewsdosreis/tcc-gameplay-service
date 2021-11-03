package br.com.lutadeclasses.gameplayservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.converter.SessaoConverter;
import br.com.lutadeclasses.gameplayservice.entity.Jornada;
import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.exception.notfound.SessaoNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.validation.SessaoComEsteTituloJaExisteException;
import br.com.lutadeclasses.gameplayservice.exception.validation.SessaoNaoEstaAbertaException;
import br.com.lutadeclasses.gameplayservice.model.SessaoStatusEnum;
import br.com.lutadeclasses.gameplayservice.model.request.NovaSessaoDto;
import br.com.lutadeclasses.gameplayservice.model.response.SessaoDto;
import br.com.lutadeclasses.gameplayservice.repository.SessaoRepository;

@Service
public class SessaoService {
    
    private SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public List<SessaoDto> listarSessoes() {
        return sessaoRepository
                    .findAll()
                    .stream()
                    .map(SessaoConverter::converterSessao)            
                    .collect(Collectors.toList());
    }

    public Sessao buscarSessao(Integer sessaoId) {
        return sessaoRepository.findById(sessaoId).orElseThrow(() -> new SessaoNaoEncontradaException(sessaoId));
    }

    public Sessao inserirSessao(NovaSessaoDto novaSessaoDto, Jornada jornada) {
        validarDadosDeSessaoAntesDeInserir(novaSessaoDto);
        var sessao = new Sessao(obj -> {
            obj.setTitulo(novaSessaoDto.getTitulo());
            obj.setStatus(SessaoStatusEnum.REGISTRADA.toString());
            obj.setJornada(jornada);
        });
        return sessaoRepository.save(sessao);
    }

    public Sessao abrirSessao(Integer sessaoId) {
        var sessao = buscarSessao(sessaoId);
        sessao.setStatus(SessaoStatusEnum.ABERTA.toString());
        return sessaoRepository.save(sessao);
    }
    
    public Sessao fecharSessao(Integer sessaoId) {
        var sessao = buscarSessao(sessaoId);
        sessao.setStatus(SessaoStatusEnum.ENCERRADA.toString());
        return sessaoRepository.save(sessao);
    }

    public void validarSeSessaoEstaAberta(Sessao sessao) {
        if (!sessao.getStatus().equals(SessaoStatusEnum.ABERTA.toString())) {
            throw new SessaoNaoEstaAbertaException(sessao.getId());
        }
    }

    private void validarDadosDeSessaoAntesDeInserir(NovaSessaoDto novaSessaoDto) {
        if (sessaoRepository.findFirstByTitulo(novaSessaoDto.getTitulo()).isPresent()) {
            throw new SessaoComEsteTituloJaExisteException(novaSessaoDto.getTitulo());
        }
    }
    
}
