package br.com.lutadeclasses.gameplayservice.exception.notfound;

public class RegistroNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegistroNaoEncontradoException(String message) {
        super(message);
    }

}
