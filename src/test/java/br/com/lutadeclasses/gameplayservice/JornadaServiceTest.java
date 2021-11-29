package br.com.lutadeclasses.gameplayservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.lutadeclasses.gameplayservice.entity.Jornada;
import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCartaDerrota;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaAlternativaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaCartaDerrotadaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.repository.JornadaCartaDerrotaRepository;
import br.com.lutadeclasses.gameplayservice.repository.JornadaCartaRepository;
import br.com.lutadeclasses.gameplayservice.repository.JornadaRepository;
import br.com.lutadeclasses.gameplayservice.service.JornadaService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class JornadaServiceTest {

    private JornadaService jornadaService;

    @Mock
    private JornadaRepository jornadaRepository;

    @Mock
    private JornadaCartaRepository jornadaCartaRepository;

    @Mock
    private JornadaCartaDerrotaRepository jornadaCartaDerrotaRepository;

    @BeforeEach
    public void prepararTestes() {
        jornadaService = new JornadaService(jornadaRepository, jornadaCartaRepository, jornadaCartaDerrotaRepository);
    }

    @Test
    void test_buscarJornada_sucesso() {
        Integer jornadaId = 1;
        var jornada = montarJornada();
        when(jornadaRepository.findById(jornadaId)).thenReturn(Optional.of(jornada));

        var expected = jornada;
        var actual = jornadaService.buscarJornada(jornadaId);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarJornada_jornadaNaoEncontrada() {
        Integer jornadaId = 1;
        when(jornadaRepository.findById(jornadaId)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(JornadaNaoEncontradaException.class, () -> {
            jornadaService.buscarJornada(jornadaId);
        });

        assertNotNull(exception);
    }

    @Test
    void test_buscarJornadaCarta_sucesso() {
        Integer jornadaCartaId = 1;
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        when(jornadaCartaRepository.findById(jornadaCartaId)).thenReturn(Optional.of(jornadaCarta));

        var expected = jornadaCarta;
        var actual = jornadaService.buscarJornadaCarta(jornadaCartaId);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarJornadaCarta_jornadaNaoEncontrada() {
        Integer jornadaCartaId = 1;
        when(jornadaCartaRepository.findById(jornadaCartaId)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(JornadaCartaNaoEncontradaException.class, () -> {
            jornadaService.buscarJornadaCarta(jornadaCartaId);
        });

        assertNotNull(exception);
    }

    @Test
    void test_buscarJornadaAlternativa_sucesso() {
        Integer jornadaAlternativaId = 1;
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        
        var expected = montarJornadaAlternativa(jornadaCarta);
        var actual = jornadaService.buscarJornadaAlternativa(jornadaCarta, jornadaAlternativaId);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarJornadaAlternativa_alternativaNaoEncontrada() {
        Integer jornadaAlternativaId = 1;
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        jornadaCarta.setJornadaAlternativaList(new ArrayList<>());

        Exception exception = assertThrows(JornadaAlternativaNaoEncontradaException.class, () -> {
            jornadaService.buscarJornadaAlternativa(jornadaCarta, jornadaAlternativaId);
        });

        assertNotNull(exception);
    }

    @Test
    void test_buscarJornadaCartaDeDerrota_sucesso() {
        Integer jornadaId = 1;
        Integer barraId = 1;
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        var jornadaCartaDerrota = montarJornadaCartaDerrota(jornadaCarta);
        when(jornadaCartaDerrotaRepository.buscarCartaDeDerrota(jornadaId, barraId)).thenReturn(jornadaCartaDerrota);

        var expected = jornadaCartaDerrota;
        var actual = jornadaService.buscarJornadaCartaDeDerrota(jornadaId, barraId);

        assertEquals(expected, actual);
    }

    @Test
    void test_buscarJornadaCartaDeDerrota_jornadaCartaDerrotaNaoEncontrada() {
        Integer jornadaId = 1;
        Integer barraId = 1;
        when(jornadaCartaDerrotaRepository.buscarCartaDeDerrota(jornadaId, barraId)).thenReturn(null);

        Exception exception = assertThrows(JornadaCartaDerrotadaNaoEncontradaException.class, () -> {
            jornadaService.buscarJornadaCartaDeDerrota(jornadaId, barraId);
        });

        assertNotNull(exception);
    }

    @Test
    void test_buscarPrimeiraCartaDaJornada_sucesso() {
        Integer jornadaId = 1;
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        when(jornadaCartaRepository.buscarPrimeiraJornadaCarta(jornadaId)).thenReturn(jornadaCarta);

        var expected = Optional.of(jornadaCarta);
        var actual = jornadaService.buscarPrimeiraCartaDaJornada(jornadaId);

        assertEquals(expected, actual);
    }
    @Test
    void test_verificarSeJornadaChegouAoFim_cartaInicial() {
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);

        boolean actual = jornadaService.verificarSeJornadaChegouAoFim(jornadaCarta);
        
        assertFalse(actual);
    }

    @Test
    void test_verificarSeJornadaChegouAoFim_comVitoria() {
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        jornadaCarta.setPosicao("VITORIA");

        boolean actual = jornadaService.verificarSeJornadaChegouAoFim(jornadaCarta);
        
        assertTrue(actual);
    }

    @Test
    void test_verificarSeJornadaChegouAoFimComVitoria_sucesso() {
        var jornada = montarJornada();
        var jornadaCarta = montarJornadaCarta(jornada);
        jornadaCarta.setPosicao("VITORIA");

        boolean actual = jornadaService.verificarSeJornadaChegouAoFimComVitoria(jornadaCarta);

        assertTrue(actual);
    }

    private Jornada montarJornada() {
        var jornada = new Jornada(obj -> {
            obj.setId(1);
            obj.setTitulo("Titulo");
        });
        return jornada;
    }

    private JornadaCarta montarJornadaCarta(Jornada jornada) {
        var jornadaCarta = new JornadaCarta(obj -> {
            obj.setId(1);
            obj.setJornada(jornada);
            obj.setPosicao("INICIO");
        });
        jornadaCarta.setJornadaAlternativaList(Arrays.asList(montarJornadaAlternativa(jornadaCarta)));
        return jornadaCarta;
    }

    private JornadaAlternativa montarJornadaAlternativa(JornadaCarta jornadaCarta) {
        var jornadaAlternativa = new JornadaAlternativa(obj -> {
            obj.setId(1);
            obj.setJornadaCarta(jornadaCarta);
            obj.setProximaJornadaCarta(jornadaCarta);
        });
        return jornadaAlternativa;
    }

    private JornadaCartaDerrota montarJornadaCartaDerrota(JornadaCarta jornadaCarta) {
        var jornadaCartaDerrota = new JornadaCartaDerrota(obj -> {
            obj.setId(1);
            obj.setJornadaCarta(jornadaCarta);
        });
        return jornadaCartaDerrota;
    }

}
