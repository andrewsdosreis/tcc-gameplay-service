package br.com.lutadeclasses.gameplayservice.exception;

public class GameOverException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private final Integer barraId;

    public GameOverException(Integer barraId) {
        super("Fim de Jogo. Você perdeu!");
        this.barraId = barraId;
    }

    public Integer getBarraId() {
        return barraId;
    }

}
