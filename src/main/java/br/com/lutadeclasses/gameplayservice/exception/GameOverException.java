package br.com.lutadeclasses.gameplayservice.exception;

public class GameOverException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public GameOverException() {
        super("Fim de Jogo. Você perdeu!");
    }

}
