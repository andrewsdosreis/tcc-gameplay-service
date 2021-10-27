package br.com.lutadeclasses.gameplayservice.exception.validation;

public class SessaoNaoEstaAbertaException extends ValidacaoException {
    
    private static final long serialVersionUID = 1L;

    public SessaoNaoEstaAbertaException(Integer sessaoId) {
        super(String.format("Sessao [id '%s'] nao esta aberta para jogadas", sessaoId));
    }

}
