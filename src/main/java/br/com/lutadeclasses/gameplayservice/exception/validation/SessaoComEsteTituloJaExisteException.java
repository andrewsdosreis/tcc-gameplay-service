package br.com.lutadeclasses.gameplayservice.exception.validation;

public class SessaoComEsteTituloJaExisteException extends ValidacaoException {
    
    private static final long serialVersionUID = 1L;

    public SessaoComEsteTituloJaExisteException(String titulo) {
        super(String.format("Sessao com titulo '%s' ja existe", titulo));
    }

}
