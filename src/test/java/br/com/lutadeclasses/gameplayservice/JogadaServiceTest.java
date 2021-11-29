package br.com.lutadeclasses.gameplayservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.entity.Jornada;
import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.entity.Usuario;
import br.com.lutadeclasses.gameplayservice.exception.validation.JogadaJaExisteException;
import br.com.lutadeclasses.gameplayservice.repository.JogadaRepository;
import br.com.lutadeclasses.gameplayservice.service.JogadaService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class JogadaServiceTest {
    
    private JogadaService jogadaService;

    @Mock
    private JogadaRepository jogadaRepository;

    @BeforeEach
    public void prepararTestes() {
        jogadaService = new JogadaService(jogadaRepository);
    }

    @Test
    void test_buscarJogadasDoPersonagem_sucesso() {
        var personagem = montarPersonagem();
        List<Jogada> jogadas = Arrays.asList(montarJogada());
        when(jogadaRepository.findByPersonagem(any())).thenReturn(jogadas);

        List<Jogada> actual = jogadaService.buscarJogadasDoPersonagem(personagem);

        assertFalse(actual.isEmpty());
    }

    @Test
    void test_buscarUltimaJogadaDoPersonagemNaJornada_sucesso() {
        Integer personagemId = 1; 
        Integer jornadaId = 1;
        var jogada = montarJogada();
        when(jogadaRepository.buscarUltimaJogadaDoPersonagemNaJornada(personagemId, jornadaId)).thenReturn(jogada);

        var expected = Optional.of(jogada);
        var actual = Optional.of(jogadaService.buscarUltimaJogadaDoPersonagemNaJornada(personagemId, jornadaId).get());

        assertEquals(expected, actual);
    }

    @Test
    void test_validarSeJogadaJaFoiExecutada_jogadaJaExiste() {
        Integer personagemId = 1; 
        Integer jornadaId = 1;
        Integer jornadaCartaId = 1;
        var jogada = montarJogada();
        when(jogadaRepository.buscarJogada(personagemId, jornadaCartaId)).thenReturn(jogada);

        Exception exception = assertThrows(JogadaJaExisteException.class, () -> {
            jogadaService.validarSeJogadaJaFoiExecutada(personagemId, jornadaId, jornadaCartaId);
        });

        assertNotNull(exception);
    }

    @Test
    void test_inserirJogada_sucesso() {
        Personagem personagem = montarPersonagem();
        JornadaCarta jornadaCarta = montarJornadaCarta();
        JornadaAlternativa jornadaAlternativa = montarJornadaAlternativa();

        jogadaService.inserirJogada(personagem, jornadaCarta, jornadaAlternativa);
        verify(jogadaRepository, times(1)).save(any());
    }

    private Jogada montarJogada() {
        var personagem = montarPersonagem();
        var jornadaCarta = montarJornadaCarta();
        var jornadaAlternativa = montarJornadaAlternativa();
        return new Jogada(obj -> {
            obj.setId(1);
            obj.setDataHora(LocalDateTime.now());
            obj.setPersonagem(personagem);
            obj.setJornadaCarta(jornadaCarta);
            obj.setJornadaAlternativa(jornadaAlternativa);
        });
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

    private JornadaCarta montarJornadaCarta() {
        var jornada = montarJornada();
        var jornadaCarta = new JornadaCarta(obj -> {
            obj.setId(1);
            obj.setJornada(jornada);
            obj.setPosicao("INICIO");
        });
        return jornadaCarta;
    }

    private JornadaAlternativa montarJornadaAlternativa() {
        var jornadaCarta = montarJornadaCarta();
        var jornadaAlternativa = new JornadaAlternativa(obj -> {
            obj.setId(1);
            obj.setJornadaCarta(jornadaCarta);
            obj.setProximaJornadaCarta(jornadaCarta);
        });
        return jornadaAlternativa;
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

}
