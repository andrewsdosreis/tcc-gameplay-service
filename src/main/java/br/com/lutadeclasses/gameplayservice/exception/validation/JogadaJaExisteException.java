package br.com.lutadeclasses.gameplayservice.exception.validation;

public class JogadaJaExisteException extends ValidacaoException {

    private static final long serialVersionUID = 1L;

    public JogadaJaExisteException(Integer personagemId, Integer jornadaId, Integer jornadaCartaId) {
        super(String.format(
                "Jogada do Personagem [id '%s'], Jornada [id '%s'] e JornadaCarta [id '%s'] ja existe",
                personagemId, jornadaId, jornadaCartaId));
    }

}
