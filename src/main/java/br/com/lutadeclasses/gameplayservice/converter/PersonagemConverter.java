package br.com.lutadeclasses.gameplayservice.converter;

import java.util.stream.Collectors;

import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;
import br.com.lutadeclasses.gameplayservice.model.response.PersonagemBarraDto;
import br.com.lutadeclasses.gameplayservice.model.response.PersonagemDto;

public interface PersonagemConverter {
    
    public static PersonagemDto converterPersonagem(Personagem personagem) {
        var personagensBarras = personagem.getPersonagemBarras()
                                          .stream()
                                          .map(PersonagemConverter::converterPersonagemBarra)
                                          .collect(Collectors.toList());
        return PersonagemDto.builder()
                            .usuarioId(personagem.getUsuario().getId())
                            .usuarioUsername(personagem.getUsuario().getUsername())
                            .personagemId(personagem.getId())
                            .personagemNome(personagem.getNome())
                            .personagemStatus(personagem.getStatus())
                            .sessaoId(personagem.getSessao().getId())
                            .sessaoTitulo(personagem.getSessao().getTitulo())
                            .barras(personagensBarras)
                            .build();
    }

    public static PersonagemBarraDto converterPersonagemBarra(PersonagemBarra personagemBarra) {
        return PersonagemBarraDto.builder()
                                 .barra(personagemBarra.getBarra().getDescricao())
                                 .valor(personagemBarra.getValor())
                                 .build();
    }

}
