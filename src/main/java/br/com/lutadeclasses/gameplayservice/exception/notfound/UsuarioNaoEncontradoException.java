package br.com.lutadeclasses.gameplayservice.exception.notfound;

public class UsuarioNaoEncontradoException extends RegistroNaoEncontradoException {
    
    private static final long serialVersionUID = 1L;

    public UsuarioNaoEncontradoException(Integer id) {
        super(String.format("Usuario [id '%s'] nao foi encontrado", id));
    }
    
}
