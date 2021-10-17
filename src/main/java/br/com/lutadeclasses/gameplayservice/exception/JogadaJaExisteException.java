package br.com.lutadeclasses.gameplayservice.exception;

public class JogadaJaExisteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JogadaJaExisteException(Integer personagemId, Integer jornadaId, Integer jornadaCartaId) {
        super(String.format(
                "Jogada do Personagem com id '%s', Jornada com id '%s' e JornadaCarta com id '%s' ja existe",
                personagemId, jornadaId, jornadaCartaId));
    }

}
