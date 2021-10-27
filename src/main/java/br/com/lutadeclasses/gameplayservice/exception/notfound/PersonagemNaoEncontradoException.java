package br.com.lutadeclasses.gameplayservice.exception.notfound;

public class PersonagemNaoEncontradoException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public PersonagemNaoEncontradoException(Integer id) {
        super(String.format("Personagem [id '%s'] nao foi encontrado", id));
    }

}
