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

import br.com.lutadeclasses.gameplayservice.converter.PersonagemConverter;
import br.com.lutadeclasses.gameplayservice.model.request.NovoPersonagemDto;
import br.com.lutadeclasses.gameplayservice.model.response.PersonagemDto;
import br.com.lutadeclasses.gameplayservice.service.PersonagemService;
import br.com.lutadeclasses.gameplayservice.service.SessaoService;

@RestController
@RequestMapping(value = "/v1/personagem")
public class PersonagemController extends BaseController {

    private PersonagemService personagemService;
    private SessaoService sessaoService;

    public PersonagemController(PersonagemService personagemService, SessaoService sessaoService) {
        this.personagemService = personagemService;
        this.sessaoService = sessaoService;
    }

    @GetMapping
    public List<PersonagemDto> listarPersonagens() {
        return personagemService.listarPersonagens();
    }

    @GetMapping(value = "/{personagemId}")
    public ResponseEntity<PersonagemDto> buscarPersonagem(@PathVariable Integer personagemId) {
        var personagem = personagemService.buscarPersonagem(personagemId);
        return ok(PersonagemConverter.converterPersonagem(personagem));
    }

    @PostMapping
    public ResponseEntity<PersonagemDto> criarPersonagem(@RequestBody @Valid NovoPersonagemDto novoPersonagemDto) {
        var sessao = sessaoService.buscarSessao(novoPersonagemDto.getSessaoId());
        var personagem = personagemService.criarPersonagem(novoPersonagemDto, sessao);
        return created(PersonagemConverter.converterPersonagem(personagem));
    }

}
