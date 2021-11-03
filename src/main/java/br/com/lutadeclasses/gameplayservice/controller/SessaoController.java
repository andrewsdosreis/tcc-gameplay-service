package br.com.lutadeclasses.gameplayservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lutadeclasses.gameplayservice.converter.SessaoConverter;
import br.com.lutadeclasses.gameplayservice.model.request.NovaSessaoDto;
import br.com.lutadeclasses.gameplayservice.model.response.SessaoDto;
import br.com.lutadeclasses.gameplayservice.service.JornadaService;
import br.com.lutadeclasses.gameplayservice.service.SessaoService;

@RestController
@RequestMapping(value = "/v1/sessao")
public class SessaoController extends BaseController {
    
    private SessaoService sessaoService;
    private JornadaService jornadaService;

    public SessaoController(SessaoService sessaoService, JornadaService jornadaService) {
        this.sessaoService = sessaoService;
        this.jornadaService = jornadaService;
    }
    
    @GetMapping
    public List<SessaoDto> listarSessoes() {
        return sessaoService.listarSessoes();
    }

    @GetMapping(value="/{sessaoId}")
    public ResponseEntity<SessaoDto> buscarSessao(@PathVariable Integer sessaoId) {
        var sessao = sessaoService.buscarSessao(sessaoId);
        return ok(SessaoConverter.converterSessao(sessao));
    }
    
    @PostMapping
    public ResponseEntity<SessaoDto> inserirSessao(@RequestBody @Valid NovaSessaoDto novaSessaoDto) {        
        var jornada = jornadaService.buscarJornada(novaSessaoDto.getJornadaId());
        var sessao = sessaoService.inserirSessao(novaSessaoDto, jornada);
        return ok(SessaoConverter.converterSessao(sessao));
    }

    @PostMapping(value = "/{sessaoId}/abrir")
    public ResponseEntity<SessaoDto> abrirSessao(@PathVariable Integer sessaoId) {
        var sessao = sessaoService.abrirSessao(sessaoId);
        return ok(SessaoConverter.converterSessao(sessao));
    }

    @PostMapping(value = "/{sessaoId}/fechar")
    public ResponseEntity<SessaoDto> fecharSessao(@PathVariable Integer sessaoId) {
        var sessao = sessaoService.fecharSessao(sessaoId);
        return ok(SessaoConverter.converterSessao(sessao));
    }

}
