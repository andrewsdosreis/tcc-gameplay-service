package br.com.lutadeclasses.gameplayservice.exception;

public class SessaoNaoEstaAbertaException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public SessaoNaoEstaAbertaException(Integer id) {
        super(String.format("Sessao com o Id '%s' nao esta aberta para jogadas", id));
    }

}
