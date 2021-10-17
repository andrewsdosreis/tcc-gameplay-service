package br.com.lutadeclasses.gameplayservice.exception;

public class JornadaCartaNaoEncontradaException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public JornadaCartaNaoEncontradaException() {
        super("Nao existe mais JornadaCarta");
    }

    public JornadaCartaNaoEncontradaException(Integer id) {
        super(String.format("JornadaCarta com o Id '%s' nao foi encontrado", id));
    }

    public JornadaCartaNaoEncontradaException(Integer personagemId, Integer jornadaId) {
        super(String.format("Nao existe mais jogadas para o Personagem com id '%s' na Jornada com id '%s'", personagemId, jornadaId));
    }


}
