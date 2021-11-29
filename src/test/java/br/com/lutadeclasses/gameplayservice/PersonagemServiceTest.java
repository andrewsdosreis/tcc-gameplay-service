package br.com.lutadeclasses.gameplayservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.lutadeclasses.gameplayservice.entity.Acao;
import br.com.lutadeclasses.gameplayservice.entity.Barra;
import br.com.lutadeclasses.gameplayservice.entity.Jornada;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;
import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.entity.Usuario;
import br.com.lutadeclasses.gameplayservice.exception.notfound.PersonagemNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.RegistroNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.UsuarioNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.validation.PersonagemNaoEstaJogandoException;
import br.com.lutadeclasses.gameplayservice.exception.validation.PersonagemNaoPertenceAJornadaException;
import br.com.lutadeclasses.gameplayservice.model.PersonagemStatusEnum;
import br.com.lutadeclasses.gameplayservice.model.request.NovoPersonagemDto;
import br.com.lutadeclasses.gameplayservice.model.response.PersonagemDto;
import br.com.lutadeclasses.gameplayservice.repository.BarraRepository;
import br.com.lutadeclasses.gameplayservice.repository.PersonagemBarraRepository;
import br.com.lutadeclasses.gameplayservice.repository.PersonagemRepository;
import br.com.lutadeclasses.gameplayservice.repository.UsuarioRepository;
import br.com.lutadeclasses.gameplayservice.service.PersonagemService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class PersonagemServiceTest {
    
    private PersonagemService personagemService;

    @Mock
    private PersonagemRepository personagemRepository;

    @Mock
    private PersonagemBarraRepository personagemBarraRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BarraRepository barraRepository;

    @BeforeEach
    public void prepararTestes() {
        personagemService = new PersonagemService(personagemRepository, personagemBarraRepository, usuarioRepository, barraRepository);
    }

    @Test
    void test_listarPersonagens_sucesso() {
        var personagem = montarPersonagem();
        List<Personagem> personagens = Arrays.asList(personagem);
        when(personagemRepository.findAll()).thenReturn(personagens);

        List<PersonagemDto> actual = personagemService.listarPersonagens();

        assertFalse(actual.isEmpty());
    }

    @Test
    void test_buscarPersonagem_sucesso() {
        Integer personagemId = 1;
        var personagem = montarPersonagem();
        when(personagemRepository.findById(personagemId)).thenReturn(Optional.of(personagem));
        
        var expected = personagem;
        var actual = personagemService.buscarPersonagem(personagemId);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarPersonagem_personagemNaoEncontrado() {
        Integer personagemId = 1;
        when(personagemRepository.findById(personagemId)).thenReturn(Optional.ofNullable(null));
        
        Exception exception = assertThrows(PersonagemNaoEncontradoException.class, () -> {
            personagemService.buscarPersonagem(personagemId);
        });

        assertNotNull(exception);
    }

    @Test
    void test_criarPersonagem_sucesso() {
        var personagem = montarPersonagem();
        var novoPersonagemDto = montarNovoPersonagemDto();
        var sessao = montarSessao();
        var barras = Arrays.asList(montarBarra());
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(montarUsuario()));
        when(barraRepository.findAll()).thenReturn(barras);
        when(personagemRepository.save(any())).thenReturn(personagem);

        var expected = personagem;
        var actual = personagemService.criarPersonagem(novoPersonagemDto, sessao);

        assertEquals(expected, actual);
    }

    @Test
    void test_criarPersonagem_usuarioNaoEncontrado() {
        var novoPersonagemDto = montarNovoPersonagemDto();
        var sessao = montarSessao();
        when(usuarioRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(UsuarioNaoEncontradoException.class, () -> {
            personagemService.criarPersonagem(novoPersonagemDto, sessao);
        });

        assertNotNull(exception);
    }

    @Test
    void test_atualizarStatusDoPersonagem_sucesso() {
        var personagem = montarPersonagem();

        personagemService.atualizarStatusDoPersonagem(personagem, PersonagemStatusEnum.VENCEDOR);
        verify(personagemRepository, times(1)).save(any());
    }

    @Test
    void test_atualizarPersonagemBarra_sucesso() {
        List<PersonagemBarra> personagemBarras = Arrays.asList(montarPersonagemBarra());
        List<Acao> acoes = Arrays.asList(montarAcao("SOMA"), montarAcao("SUBTRAI"), montarAcao("MULTIPLICA"));
        
        personagemService.atualizarPersonagemBarra(personagemBarras, acoes);
        
        verify(personagemBarraRepository, times(1)).saveAll(personagemBarras);
    }

    @Test
    void test_validarSePersonagemEstaNaJornada_sucesso() {
        Integer jornadaId = 1;
        var personagem = montarPersonagem();

        personagemService.validarSePersonagemEstaNaJornada(personagem, jornadaId);
    }

    @Test
    void test_validarSePersonagemEstaNaJornada_personagemNaoEstaNaJornada() {
        var personagem = montarPersonagem();

        Exception exception = assertThrows(PersonagemNaoPertenceAJornadaException.class, () -> {
            personagemService.validarSePersonagemEstaNaJornada(personagem, 2);
        });

        assertNotNull(exception);
    }

    @Test
    void test_validarSePersonagemAindaEstaJogando_usuarioNaoEstaMaisJogando() {
        var personagem = montarPersonagem();
        personagem.setStatus("DERROTADO");
        
        Exception exception = assertThrows(PersonagemNaoEstaJogandoException.class, () -> {
            personagemService.validarSePersonagemAindaEstaJogando(personagem);
        });

        assertNotNull(exception);
    }

    @Test
    void test_validarSePersonagemFoiDerrotado_sucesso() {
        var personagem = montarPersonagem();
        personagem.setPersonagemBarras(Arrays.asList(montarPersonagemBarra()));

        var actual = personagemService.validarSePersonagemFoiDerrotado(personagem);

        assertFalse(actual);
    }

    @Test
    void test_buscarBarraQueCausouDerrotaDoPersonagem_sucesso() {
        var personagem = montarPersonagem();
        var personagemBarra = montarPersonagemBarra();
        personagemBarra.setValor(0);
        personagem.setPersonagemBarras(Arrays.asList(personagemBarra));

        var expected = personagemBarra;
        var actual = personagemService.buscarBarraQueCausouDerrotaDoPersonagem(personagem);

        assertEquals(expected, actual);
    }

    @Test 
    void test_buscarBarraQueCausouDerrotaDoPersonagem_personagemNaoPerdeu() {
        var personagem = montarPersonagem();
        personagem.setPersonagemBarras(Arrays.asList(montarPersonagemBarra()));

        Exception exception = assertThrows(RegistroNaoEncontradoException.class, () -> {
            personagemService.buscarBarraQueCausouDerrotaDoPersonagem(personagem);
        });

        assertNotNull(exception);
    }

    private Personagem montarPersonagem() {
        var personagem = new Personagem(obj -> {
            obj.setId(1);
            obj.setNome("nome");
            obj.setStatus("JOGANDO");
            obj.setPersonagemBarras(new ArrayList<>());
            obj.setUsuario(montarUsuario());
            obj.setSessao(montarSessao());
        });
        return personagem;
    }

    private PersonagemBarra montarPersonagemBarra() {
        var personagem = montarPersonagem();
        var barra = montarBarra();
        return new PersonagemBarra(obj -> {
            obj.setId(1);
            obj.setBarra(barra);
            obj.setPersonagem(personagem);
            obj.setValor(100);
        });
    }
 
    private Sessao montarSessao() {
        return new Sessao(obj -> {
            obj.setId(1);
            obj.setStatus("status");
            obj.setTitulo("titulo");
            obj.setJornada(montarJornada());
        });
    }
    
    private Jornada montarJornada() {
        return new Jornada(obj -> {
            obj.setId(1);
            obj.setTitulo("Titulo");
        });
    }

    private Usuario montarUsuario() {
        return new Usuario(usuario -> {
            usuario.setId(1);
            usuario.setEmail("nomedeusuario@email.com");
            usuario.setNome("Nome");
            usuario.setSobrenome("sobrenome");
            usuario.setUsername("username");
        });
    }

    private Acao montarAcao(String tipo) {
        var barra = montarBarra();
        return new Acao(obj -> {
            obj.setId(1);
            obj.setTipo(tipo);
            obj.setValor(10);
            obj.setBarra(barra);
        });
    }

    private Barra montarBarra() {
        return new Barra(obj -> {
            obj.setId(1);
            obj.setDescricao("VIDA");
        });
    }

    private NovoPersonagemDto montarNovoPersonagemDto() {
        return NovoPersonagemDto
                .builder()
                .nome("nome")
                .sessaoId(1)
                .usuarioId(1)
                .build();
    }

}
