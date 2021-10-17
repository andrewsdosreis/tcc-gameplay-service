package br.com.lutadeclasses.gameplayservice.exception;

public class PersonagemNaoEncontradoException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public PersonagemNaoEncontradoException(Integer id) {
        super(String.format("Personagem com o Id '%s' nao foi encontrado", id));
    }

}
