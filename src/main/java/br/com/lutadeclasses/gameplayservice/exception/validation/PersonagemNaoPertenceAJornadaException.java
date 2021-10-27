package br.com.lutadeclasses.gameplayservice.exception.validation;

public class PersonagemNaoPertenceAJornadaException extends ValidacaoException {
    
    private static final long serialVersionUID = 1L;

    public PersonagemNaoPertenceAJornadaException(Integer personagemId, Integer jornadaId) {
        super(String.format("Personagem [id '%s'] nao pertence a Jornada [id '%s']", personagemId, jornadaId));
    }

}
