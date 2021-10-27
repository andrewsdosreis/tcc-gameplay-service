package br.com.lutadeclasses.gameplayservice.exception.notfound;

public class JornadaCartaDerrotadaNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public JornadaCartaDerrotadaNaoEncontradaException(Integer jornadaId, Integer barraId) {
        super(String.format("Nao existe carta de derrota para Barra [id '%s'] na Jornada [id '%s']", barraId, jornadaId));
    }

}
