package br.com.lutadeclasses.gameplayservice.exception.notfound;

public class JornadaAlternativaNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public JornadaAlternativaNaoEncontradaException(Integer jornadaAlternativaId) {
        super(String.format("JornadaAlternativa [id '%s'] nao foi encontrada", jornadaAlternativaId));
    }

    public JornadaAlternativaNaoEncontradaException(Integer jornadaCartaId, Integer jornadaAlternativaId) {
        super(String.format("JornadaAlternativa [id '%s'] na JornadaCarta [id '%s'] nao foi encontrada", jornadaAlternativaId, jornadaCartaId));
    }

}
