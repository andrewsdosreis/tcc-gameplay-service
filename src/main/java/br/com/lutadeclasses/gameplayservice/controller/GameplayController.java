package br.com.lutadeclasses.gameplayservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.gameplayservice.model.request.JogadaDto;
import br.com.lutadeclasses.gameplayservice.model.response.ProximaJogadaDto;
import br.com.lutadeclasses.gameplayservice.service.GameplayService;

@RestController
@RequestMapping(value = "/gameplay")
public class GameplayController extends BaseController {
    
    private GameplayService gameplayService;

    public GameplayController(GameplayService gameplayService) {
        this.gameplayService = gameplayService;
    }

    @GetMapping
    public ResponseEntity<ProximaJogadaDto> buscarProximajogada(@RequestParam Integer personagemId, @RequestParam Integer jornadaId) {
        return ok(gameplayService.buscarProximaJogada(personagemId, jornadaId));
    }

    @PostMapping
    public ResponseEntity<ProximaJogadaDto> fazerJogada(@RequestBody JogadaDto proximaJogadaDto) {
        return ok(gameplayService.fazerJogada(proximaJogadaDto));
    }

}
