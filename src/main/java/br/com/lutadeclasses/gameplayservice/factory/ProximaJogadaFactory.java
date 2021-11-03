package br.com.lutadeclasses.gameplayservice.factory;

import java.util.List;
import java.util.stream.Collectors;

import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.model.response.AlternativaDto;
import br.com.lutadeclasses.gameplayservice.model.response.PersonagemBarraDto;
import br.com.lutadeclasses.gameplayservice.model.response.ProximaJogadaDto;

public interface ProximaJogadaFactory {

    public static ProximaJogadaDto build(Personagem personagem, JornadaCarta jornadaCarta) {
        return ProximaJogadaDto
                .builder()
                .personagemId(personagem.getId())
                .personagemNome(personagem.getNome())
                .jornadaId(jornadaCarta.getJornada().getId())
                .jornadaCartaId(jornadaCarta.getId())
                .carta(ajustaDescricaoDaCarta(jornadaCarta.getCarta().getDescricao(), personagem.getNome()))
                .ator(jornadaCarta.getCarta().getAtor())
                .alternativas(montarListaDeAlternativas(jornadaCarta))
                .barras(montarListaDeBarrasDoPersonagem(personagem))
                .build();
    }

    private static List<AlternativaDto> montarListaDeAlternativas(JornadaCarta jornadaCarta) {
        return jornadaCarta
                .getJornadaAlternativaList()
                .stream()
                .map(jornadaAlternativa -> AlternativaDto
                                            .builder()
                                            .jornadaAlternativaId(jornadaAlternativa.getId())
                                            .descricao(jornadaAlternativa.getAlternativa().getDescricao())
                                            .build())
                .collect(Collectors.toList());
    }

    private static List<PersonagemBarraDto> montarListaDeBarrasDoPersonagem(Personagem personagem) {
        return personagem
                .getPersonagemBarras()
                .stream()
                .map(personagemBarra -> PersonagemBarraDto
                                            .builder()
                                            .barra(personagemBarra.getBarra().getDescricao())
                                            .valor(personagemBarra.getValor())
                                            .build())
                .collect(Collectors.toList());
    }

    private static String ajustaDescricaoDaCarta(String cartaDescricao, String nomeJogador) {
        return cartaDescricao.replace("$nome_jogador", nomeJogador);
    }
}
