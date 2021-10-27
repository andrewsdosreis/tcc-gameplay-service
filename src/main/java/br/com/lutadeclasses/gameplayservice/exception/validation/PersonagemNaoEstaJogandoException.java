package br.com.lutadeclasses.gameplayservice.exception.validation;

public class PersonagemNaoEstaJogandoException extends ValidacaoException {
    
    private static final long serialVersionUID = 1L;

    public PersonagemNaoEstaJogandoException(Integer personagemId) {
        super(String.format("Personagem [id '%s'] nao esta com o status JOGANDO", personagemId));
    }

}
