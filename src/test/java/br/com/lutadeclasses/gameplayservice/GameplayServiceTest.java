package br.com.lutadeclasses.gameplayservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import br.com.lutadeclasses.gameplayservice.entity.Alternativa;
import br.com.lutadeclasses.gameplayservice.entity.Barra;
import br.com.lutadeclasses.gameplayservice.entity.Carta;
import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.entity.Jornada;
import br.com.lutadeclasses.gameplayservice.entity.JornadaAlternativa;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCarta;
import br.com.lutadeclasses.gameplayservice.entity.JornadaCartaDerrota;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;
import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.entity.Usuario;
import br.com.lutadeclasses.gameplayservice.exception.notfound.JornadaCartaNaoEncontradaException;
import br.com.lutadeclasses.gameplayservice.model.request.JogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.ProximaJogadaDto;
import br.com.lutadeclasses.gameplayservice.service.GameplayService;
import br.com.lutadeclasses.gameplayservice.service.JogadaService;
import br.com.lutadeclasses.gameplayservice.service.JornadaService;
import br.com.lutadeclasses.gameplayservice.service.PersonagemService;
import br.com.lutadeclasses.gameplayservice.service.SessaoService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class GameplayServiceTest {

    private GameplayService gameplayService;
    
    @Mock
    private JornadaService jornadaService;

    @Mock
    private PersonagemService personagemService;

    @Mock
    private JogadaService jogadaService;

    @Mock
    private SessaoService sessaoService;

    @BeforeEach
    public void prepararTestes() {
        gameplayService = new GameplayService(jornadaService, personagemService, jogadaService, sessaoService);
    }

    @Test
    void test_ProximaJogadaDto_primeiraJogada() {
        Integer personagemId = 1;
        Integer jornadaId = 1;
        var personagem = montarPersonagem();
        var jornadaCarta = montarJornadaCarta();
        when(personagemService.buscarPersonagem(personagemId)).thenReturn(personagem);
        when(jogadaService.buscarUltimaJogadaDoPersonagemNaJornada(personagemId, jornadaId)).thenReturn(Optional.ofNullable(null));
        when(jornadaService.buscarPrimeiraCartaDaJornada(jornadaId)).thenReturn(Optional.of(jornadaCarta));

        ProximaJogadaDto actual = gameplayService.buscarProximaJogada(personagemId, jornadaId);
        assertEquals(jornadaCarta.getCarta().getDescricao(), actual.getCarta());
        assertEquals(jornadaCarta.getCarta().getAtor(), actual.getAtor());
    }

    @Test
    void test_ProximaJogadaDto_comUltimaJogada() {
        Integer personagemId = 1;
        Integer jornadaId = 1;
        var personagem = montarPersonagem();
        var jornadaCarta = montarJornadaCarta();
        var jogada = montarJogada();
        when(personagemService.buscarPersonagem(personagemId)).thenReturn(personagem);
        when(jogadaService.buscarUltimaJogadaDoPersonagemNaJornada(personagemId, jornadaId)).thenReturn(Optional.of(jogada));
        when(jornadaService.verificarSeJornadaChegouAoFim(any())).thenReturn(Boolean.TRUE);

        ProximaJogadaDto actual = gameplayService.buscarProximaJogada(personagemId, jornadaId);
        assertEquals(jornadaCarta.getCarta().getDescricao(), actual.getCarta());
        assertEquals(jornadaCarta.getCarta().getAtor(), actual.getAtor());
    }

    @Test
    void test_ProximaJogadaDto_erroNaProximaCarta() {
        Integer personagemId = 1;
        Integer jornadaId = 1;
        var personagem = montarPersonagem();
        var jogada = montarJogada();
        jogada.getJornadaAlternativa().setProximaJornadaCarta(null);
        when(personagemService.buscarPersonagem(personagemId)).thenReturn(personagem);
        when(jogadaService.buscarUltimaJogadaDoPersonagemNaJornada(personagemId, jornadaId)).thenReturn(Optional.ofNullable(null));
        when(jornadaService.buscarPrimeiraCartaDaJornada(jornadaId)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(JornadaCartaNaoEncontradaException.class, () -> {
            gameplayService.buscarProximaJogada(personagemId, jornadaId);
        });

        assertNotNull(exception);
    }

    @Test
    void test_fazerJogada_simples() {
        var jogadaDto = montarJogadaDto();
        var personagem = montarPersonagem();
        var jornadaCarta = montarJornadaCarta();
        var jornadaAlternativa = montarJornadaAlternativa(jornadaCarta);
        var jogada = montarJogada();

        when(personagemService.buscarPersonagem(any())).thenReturn(personagem);
        when(personagemService.validarSePersonagemFoiDerrotado(any())).thenReturn(Boolean.FALSE);
        when(jornadaService.buscarJornadaCarta(any())).thenReturn(jornadaCarta);
        when(jornadaService.verificarSeJornadaChegouAoFimComVitoria(any())).thenReturn(Boolean.FALSE);
        when(jornadaService.buscarJornadaAlternativa(any(), any())).thenReturn(jornadaAlternativa);
        lenient().when(jogadaService.inserirJogada(any(), any(), any())).thenReturn(jogada);

        var actual = gameplayService.fazerJogada(jogadaDto);
        assertEquals(jornadaCarta.getCarta().getDescricao(), actual.getCarta());
        assertEquals(jornadaCarta.getCarta().getAtor(), actual.getAtor());        
    }

    @Test
    void test_fazerJogada_fimDeJogoComVitoria() {
        var jogadaDto = montarJogadaDto();
        var personagem = montarPersonagem();
        var jornadaCarta = montarJornadaCarta();
        var jornadaAlternativa = montarJornadaAlternativa(jornadaCarta);
        var jogada = montarJogada();

        when(personagemService.buscarPersonagem(any())).thenReturn(personagem);
        when(personagemService.validarSePersonagemFoiDerrotado(any())).thenReturn(Boolean.FALSE);
        when(jornadaService.buscarJornadaCarta(any())).thenReturn(jornadaCarta);
        when(jornadaService.verificarSeJornadaChegouAoFimComVitoria(any())).thenReturn(Boolean.TRUE);
        when(jornadaService.buscarJornadaAlternativa(any(), any())).thenReturn(jornadaAlternativa);
        lenient().when(jogadaService.inserirJogada(any(), any(), any())).thenReturn(jogada);

        var actual = gameplayService.fazerJogada(jogadaDto);
        assertEquals(jornadaCarta.getCarta().getDescricao(), actual.getCarta());
        assertEquals(jornadaCarta.getCarta().getAtor(), actual.getAtor());
    }

    @Test
    void test_fazerJogada_fimDeJogoComDerrota() {
        var jogadaDto = montarJogadaDto();
        var personagem = montarPersonagem();
        var personagemBarra = montarPersonagemBarra();
        var jornadaCarta = montarJornadaCarta();
        var jornadaAlternativa = montarJornadaAlternativa(jornadaCarta);
        var jornadaCartaDerrota = montarJornadaCartaDerrota(jornadaCarta);
        var jogada = montarJogada();

        when(personagemService.buscarPersonagem(any())).thenReturn(personagem);
        when(personagemService.validarSePersonagemFoiDerrotado(any())).thenReturn(Boolean.TRUE);
        when(personagemService.buscarBarraQueCausouDerrotaDoPersonagem(any())).thenReturn(personagemBarra);
        when(jornadaService.buscarJornadaCarta(any())).thenReturn(jornadaCarta);
        when(jornadaService.buscarJornadaAlternativa(any(), any())).thenReturn(jornadaAlternativa);
        when(jornadaService.buscarJornadaCartaDeDerrota(any(), any())).thenReturn(jornadaCartaDerrota);
        lenient().when(jogadaService.inserirJogada(any(), any(), any())).thenReturn(jogada);

        var actual = gameplayService.fazerJogada(jogadaDto);
        assertEquals(jornadaCarta.getCarta().getDescricao(), actual.getCarta());
        assertEquals(jornadaCarta.getCarta().getAtor(), actual.getAtor());
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

    private JornadaCarta montarJornadaCarta() {
        var jornada = montarJornada();
        var carta = montarCarta();
        var jornadaCarta = new JornadaCarta(obj -> {
            obj.setId(1);
            obj.setJornada(jornada);
            obj.setPosicao("INICIO");
            obj.setCarta(carta);
        });
        jornadaCarta.setJornadaAlternativaList(Arrays.asList(montarJornadaAlternativa(jornadaCarta)));
        return jornadaCarta;
    }

    private JornadaAlternativa montarJornadaAlternativa(JornadaCarta jornadaCarta) {
        var carta = montarCarta();
        var alternativa = montarAlternativa(carta);
        var jornadaAlternativa = new JornadaAlternativa(obj -> {
            obj.setId(1);
            obj.setJornadaCarta(jornadaCarta);
            obj.setProximaJornadaCarta(jornadaCarta);
            obj.setAlternativa(alternativa);
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

    private Usuario montarUsuario() {
        return new Usuario(usuario -> {
            usuario.setId(1);
            usuario.setEmail("nomedeusuario@email.com");
            usuario.setNome("Nome");
            usuario.setSobrenome("sobrenome");
            usuario.setUsername("username");
        });
    }

    private Carta montarCarta() {
        Carta carta = new Carta(obj -> {
            obj.setId(1);
            obj.setDescricao("Carta");
            obj.setAtor("Ator");
            obj.setAlternativas(new ArrayList<>());
        });
        List<Alternativa> alternativas = new ArrayList<>();
        alternativas.add(montarAlternativa(carta));
        carta.setAlternativas(alternativas);
        return carta;
    }

    private Alternativa montarAlternativa(Carta carta) {
        Alternativa alternativa = new Alternativa(obj -> {
            obj.setId(1);
            obj.setDescricao("Alternativa");
            obj.setCarta(carta);
        });
        List<Acao> acoes = new ArrayList<>();
        acoes.add(montarAcao(alternativa));
        alternativa.setAcoes(acoes);
        return alternativa;
    }

    private Acao montarAcao(Alternativa alternativa) {
        var barra = montarBarra();
        return new Acao(obj -> {
            obj.setId(1);
            obj.setTipo("SOMA");
            obj.setValor(10);
            obj.setBarra(barra);
            obj.setAlternativa(alternativa);
        });
    }

    private Barra montarBarra() {
        return new Barra(obj -> {
            obj.setId(1);
            obj.setDescricao("VIDA");
        });
    }

    private Jogada montarJogada() {
        var personagem = montarPersonagem();
        var jornadaCarta = montarJornadaCarta();
        var jornadaAlternativa = montarJornadaAlternativa(jornadaCarta);
        return new Jogada(obj -> {
            obj.setId(1);
            obj.setDataHora(LocalDateTime.now());
            obj.setPersonagem(personagem);
            obj.setJornadaCarta(jornadaCarta);
            obj.setJornadaAlternativa(jornadaAlternativa);
        });
    }

    private JogadaDto montarJogadaDto() {
        return JogadaDto
                .builder()
                .personagemId(1)
                .jornadaId(1)
                .jornadaCartaId(1)
                .jornadaAlternativaId(1)
                .build();
    }
    
}
