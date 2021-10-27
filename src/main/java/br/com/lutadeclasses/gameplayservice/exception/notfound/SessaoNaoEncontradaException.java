package br.com.lutadeclasses.gameplayservice.exception.notfound;

public class SessaoNaoEncontradaException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public SessaoNaoEncontradaException(Integer sessaoId) {
        super(String.format("Sessao [id '%s'] nao encontrada", sessaoId));
    }

}
