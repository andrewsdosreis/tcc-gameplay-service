package br.com.lutadeclasses.gameplayservice.exception;

public class JornadaAlternativaNaoEncontradaException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public JornadaAlternativaNaoEncontradaException(Integer id) {
        super(String.format("JornadaAlternativa com o Id '%s' nao foi encontrado", id));
    }

    public JornadaAlternativaNaoEncontradaException(Integer jornadaCartaId, Integer alternativaId) {
        super(String.format("JornadaAlternativa com JornadaCartaId '%s' e AlternativaId '%s' nao foi encontrada", jornadaCartaId, alternativaId));
    }

}
