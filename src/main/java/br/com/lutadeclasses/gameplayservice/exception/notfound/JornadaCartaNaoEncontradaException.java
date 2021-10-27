package br.com.lutadeclasses.gameplayservice.exception.notfound;

public class JornadaCartaNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public JornadaCartaNaoEncontradaException(Integer jornadaCartaId) {
        super(String.format("JornadaCarta [id '%s'] nao foi encontrado", jornadaCartaId));
    }

    public JornadaCartaNaoEncontradaException(Integer personagemId, Integer jornadaId) {
        super(String.format("Nao existe mais jogadas para o Personagem [id '%s'] na Jornada [id '%s']", personagemId, jornadaId));
    }

}
