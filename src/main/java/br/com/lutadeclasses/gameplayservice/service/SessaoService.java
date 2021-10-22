package br.com.lutadeclasses.gameplayservice.service;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.exception.SessaoNaoEstaAbertaException;
import br.com.lutadeclasses.gameplayservice.model.SessaoStatusEnum;
import br.com.lutadeclasses.gameplayservice.repository.SessaoRepository;

@Service
public class SessaoService {
    
    private SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public void validarSeSessaoEstaAberta(Sessao sessao) {
        if (!sessao.getStatus().equals(SessaoStatusEnum.ABERTA.toString())) {
            throw new SessaoNaoEstaAbertaException(sessao.getId());
        }
    }
    
}
