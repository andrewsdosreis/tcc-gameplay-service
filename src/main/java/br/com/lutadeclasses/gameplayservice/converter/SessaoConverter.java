package br.com.lutadeclasses.gameplayservice.converter;

import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.model.response.SessaoDto;

public interface SessaoConverter {

    static SessaoDto converterSessao(Sessao sessao) {
        return SessaoDto.builder()
                        .titulo(sessao.getTitulo())
                        .status(sessao.getStatus())
                        .jornadaId(sessao.getJornada().getId())
                        .jornadaTitulo(sessao.getJornada().getTitulo())
                        .build();
    }
    
}
