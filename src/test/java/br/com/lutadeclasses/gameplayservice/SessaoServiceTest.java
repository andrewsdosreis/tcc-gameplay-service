package br.com.lutadeclasses.gameplayservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

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

import br.com.lutadeclasses.gameplayservice.entity.Jornada;
import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.exception.notfound.SessaoNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.validation.SessaoComEsteTituloJaExisteException;
import br.com.lutadeclasses.gameplayservice.model.request.NovaSessaoDto;
import br.com.lutadeclasses.gameplayservice.model.response.SessaoDto;
import br.com.lutadeclasses.gameplayservice.repository.SessaoRepository;
import br.com.lutadeclasses.gameplayservice.service.SessaoService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class SessaoServiceTest {
    
    private SessaoService sessaoService;

    @Mock
    private SessaoRepository sessaoRepository;

    @BeforeEach
    public void prepararTestes() {
        sessaoService = new SessaoService(sessaoRepository);
    }

    @Test
    void test_listarSessoes_sucesso() {
        var sessao = montarSessao();
        List<Sessao> sessoes = Arrays.asList(sessao);
        when(sessaoRepository.findAll()).thenReturn(sessoes);

        List<SessaoDto> actual = sessaoService.listarSessoes();

        assertFalse(actual.isEmpty());
    }

    @Test
    void test_buscarSessao_sucesso() {
        var sessao = montarSessao();
        when(sessaoRepository.findById(anyInt())).thenReturn(Optional.of(sessao));

        var expected = sessao;
        var actual = sessaoService.buscarSessao(1);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarSessao_sessaoNaoEncontrada() {
        when(sessaoRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(SessaoNaoEncontradaException.class, () -> {
            sessaoService.buscarSessao(1);
        });

        assertNotNull(exception);
    }

    @Test
    void test_inserirSessao_sucesso() {
        var novaSessaoDto = montarNovaSessaoDto();
        var sessao = montarSessao();
        var jornada = montarJornada();
        when(sessaoRepository.findFirstByTitulo(anyString())).thenReturn(Optional.ofNullable(null));
        lenient().when(sessaoRepository.save(any())).thenReturn(sessao);
        
        var expected = sessao;
        var actual = sessaoService.inserirSessao(novaSessaoDto, jornada);

        assertEquals(expected, actual);
    }

    @Test
    void test_inserirSessao_sessaoComEsteTituloJaExiste() {
        var novaSessaoDto = montarNovaSessaoDto();
        var sessao = montarSessao();
        var jornada = montarJornada();
        when(sessaoRepository.findFirstByTitulo(anyString())).thenReturn(Optional.of(sessao));

        Exception exception = assertThrows(SessaoComEsteTituloJaExisteException.class, () -> {
            sessaoService.inserirSessao(novaSessaoDto, jornada);
        });

        assertNotNull(exception);
    }

    @Test
    void test_abrirSessao_sucesso() {
        var sessao = montarSessao();
        sessao.setStatus("ABERTA");
        when(sessaoRepository.findById(anyInt())).thenReturn(Optional.of(sessao));
        lenient().when(sessaoRepository.save(any())).thenReturn(sessao);

        var expected = sessao;
        var actual = sessaoService.abrirSessao(1);

        assertEquals(expected, actual);
    }

    @Test
    void test_fecharSessao_sucesso() {
        var sessao = montarSessao();
        sessao.setStatus("FECHADA");
        when(sessaoRepository.findById(anyInt())).thenReturn(Optional.of(sessao));
        lenient().when(sessaoRepository.save(any())).thenReturn(sessao);

        var expected = sessao;
        var actual = sessaoService.abrirSessao(1);

        assertEquals(expected, actual);
    }

    private Sessao montarSessao() {
        return new Sessao(obj -> {
            obj.setId(1);
            obj.setStatus("REGISTRADA");
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

    private NovaSessaoDto montarNovaSessaoDto() {
        return NovaSessaoDto
                .builder()
                .titulo("titulo")
                .jornadaId(1)
                .build();
    }
}
