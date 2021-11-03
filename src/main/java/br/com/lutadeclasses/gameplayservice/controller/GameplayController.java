package br.com.lutadeclasses.gameplayservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.gameplayservice.converter.JogadaConverter;
import br.com.lutadeclasses.gameplayservice.entity.Jogada;
import br.com.lutadeclasses.gameplayservice.model.request.JogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.ProximaJogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.ResumoPersonagemDto;
import br.com.lutadeclasses.gameplayservice.service.GameplayService;
import br.com.lutadeclasses.gameplayservice.service.JogadaService;
import br.com.lutadeclasses.gameplayservice.service.PersonagemService;

@RestController
@RequestMapping(value = "/v1/gameplay")
public class GameplayController extends BaseController {
    
    private GameplayService gameplayService;
    private JogadaService jogadaService;
    private PersonagemService personagemService;
    
    public GameplayController(GameplayService gameplayService, JogadaService jogadaService,
            PersonagemService personagemService) {
        this.gameplayService = gameplayService;
        this.jogadaService = jogadaService;
        this.personagemService = personagemService;
    }

    @GetMapping
    public ResponseEntity<ProximaJogadaDto> buscarProximajogada(@RequestParam Integer personagemId, @RequestParam Integer jornadaId) {
        return ok(gameplayService.buscarProximaJogada(personagemId, jornadaId));
    }

    @PostMapping
    public ResponseEntity<ProximaJogadaDto> fazerJogada(@RequestBody JogadaDto proximaJogadaDto) {
        return ok(gameplayService.fazerJogada(proximaJogadaDto));
    }

    @GetMapping(value = "/{personagemId}")
    public ResponseEntity<ResumoPersonagemDto> buscarResumoDeJogadasDoPersonagem(@PathVariable Integer personagemId) {
        var personagem = personagemService.buscarPersonagem(personagemId);
        List<Jogada> jogadas = jogadaService.buscarJogadasDoPersonagem(personagem);
        return ok(JogadaConverter.montarResumoDoPersonagem(personagem, jogadas));
    }

}
