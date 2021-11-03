package br.com.lutadeclasses.gameplayservice.converter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;

import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.model.response.JogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.PersonagemBarraDto;
import br.com.lutadeclasses.gameplayservice.model.response.ResumoPersonagemDto;

public interface JogadaConverter {
    
    public static ResumoPersonagemDto montarResumoDoPersonagem(Personagem personagem, List<Jogada> jogadas) {
        List<JogadaDto> jogadasDto = jogadas.stream()
                                            .map(JogadaConverter::montarJogadas)
                                            .collect(Collectors.toList());

        List<PersonagemBarraDto> barras = personagem.getPersonagemBarras()
                                                    .stream()
                                                    .map(PersonagemConverter::converterPersonagemBarra)
                                                    .collect(Collectors.toList());

        return ResumoPersonagemDto.builder()
                                  .usuarioId(personagem.getUsuario().getId())
                                  .personagemId(personagem.getId())
                                  .personagemNome(personagem.getNome())
                                  .personagemStatus(personagem.getStatus())
                                  .barras(barras)
                                  .jogadas(jogadasDto)
                                  .build();
    }

    private static JogadaDto montarJogadas(Jogada jogada) {
        return JogadaDto.builder()
                        .jornadaCartaId(jogada.getJornadaCarta().getId())
                        .cartaDescricao(jogada.getJornadaCarta().getCarta().getDescricao())
                        .jornadaAlternativaId(montarAlternativaId(jogada))
                        .alternativaDescricao(montarAlternativaDescricao(jogada))
                        .dataHora(jogada.getDataHora())
                        .build();
    }

    private static Integer montarAlternativaId(Jogada jogada) {
        return Objects.isNull(jogada.getJornadaAlternativa()) ? 0 : jogada.getJornadaAlternativa().getId();
    }

    private static String montarAlternativaDescricao(Jogada jogada) {
        return Objects.isNull(jogada.getJornadaAlternativa()) ? 
            Strings.EMPTY : jogada.getJornadaAlternativa().getAlternativa().getDescricao();
    }

}
