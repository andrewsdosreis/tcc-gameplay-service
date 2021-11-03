package br.com.lutadeclasses.gameplayservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.converter.PersonagemConverter;
import br.com.lutadeclasses.gameplayservice.entity.Acao;
import br.com.lutadeclasses.gameplayservice.entity.Barra;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;
import br.com.lutadeclasses.gameplayservice.entity.Sessao;
import br.com.lutadeclasses.gameplayservice.exception.notfound.PersonagemNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.RegistroNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.UsuarioNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.validation.PersonagemNaoEstaJogandoException;
import br.com.lutadeclasses.gameplayservice.exception.validation.PersonagemNaoPertenceAJornadaException;
import br.com.lutadeclasses.gameplayservice.model.AcaoTipoEnum;
import br.com.lutadeclasses.gameplayservice.model.PersonagemStatusEnum;
import br.com.lutadeclasses.gameplayservice.model.request.NovoPersonagemDto;
import br.com.lutadeclasses.gameplayservice.model.response.PersonagemDto;
import br.com.lutadeclasses.gameplayservice.repository.BarraRepository;
import br.com.lutadeclasses.gameplayservice.repository.PersonagemBarraRepository;
import br.com.lutadeclasses.gameplayservice.repository.PersonagemRepository;
import br.com.lutadeclasses.gameplayservice.repository.UsuarioRepository;

@Service
public class PersonagemService {
    
    private static final List<String> PERSONAGEM_PODE_JOGAR = Arrays.asList(PersonagemStatusEnum.REGISTRADO.toString(), PersonagemStatusEnum.JOGANDO.toString());

    private PersonagemRepository personagemRepository;
    private PersonagemBarraRepository personagemBarraRepository;
    private UsuarioRepository usuarioRepository;
    private BarraRepository barraRepository;
    
    public PersonagemService(PersonagemRepository personagemRepository,
            PersonagemBarraRepository personagemBarraRepository, UsuarioRepository usuarioRepository,
            BarraRepository barraRepository) {
        this.personagemRepository = personagemRepository;
        this.personagemBarraRepository = personagemBarraRepository;
        this.usuarioRepository = usuarioRepository;
        this.barraRepository = barraRepository;
    }

    public List<PersonagemDto> listarPersonagens() {
        return personagemRepository.findAll()
                                   .stream()
                                   .map(PersonagemConverter::converterPersonagem)
                                   .collect(Collectors.toList());
    }

    public Personagem buscarPersonagem(Integer personagemId) {
        return personagemRepository.findById(personagemId)
                                   .orElseThrow(() -> new PersonagemNaoEncontradoException(personagemId));
    }    

    public Personagem criarPersonagem(NovoPersonagemDto novoPersonagemDto, Sessao sessao) {       
        var usuario = usuarioRepository.findById(novoPersonagemDto.getUsuarioId())
                                       .orElseThrow(() -> new UsuarioNaoEncontradoException(novoPersonagemDto.getUsuarioId()));
        var personagem = new Personagem(obj -> {
            obj.setNome(novoPersonagemDto.getNome());
            obj.setSessao(sessao);
            obj.setUsuario(usuario);
            obj.setStatus(PersonagemStatusEnum.REGISTRADO.toString());
        });
        personagem.setPersonagemBarras(criarListaDeBarrasDoPersonagem(personagem));
        return personagemRepository.save(personagem);
    }

    public void atualizarStatusDoPersonagem(Personagem personagem, PersonagemStatusEnum status) {
        personagem.setStatus(status.toString());
        personagemRepository.save(personagem);
    }

    public List<PersonagemBarra> atualizarPersonagemBarra(List<PersonagemBarra> personagemBarraList, List<Acao> acaoList) {
        personagemBarraList
            .stream()
            .forEach(personagemBarra -> {
                for (Acao acao : acaoList) {
                    if (personagemBarra.getBarra().getId().equals(acao.getBarra().getId())) {
                        if (acao.getTipo().equals(AcaoTipoEnum.SOMA.toString())) {
                            personagemBarra.setValor(personagemBarra.getValor() + acao.getValor());
                        }

                        if (acao.getTipo().equals(AcaoTipoEnum.SUBTRAI.toString())) {
                            personagemBarra.setValor(personagemBarra.getValor() - acao.getValor());
                        }

                        if (acao.getTipo().equals(AcaoTipoEnum.MULTIPLICA.toString())) {
                            personagemBarra.setValor(personagemBarra.getValor() * acao.getValor());
                        }
                    }
                }
            });

        return personagemBarraRepository.saveAll(personagemBarraList);
    }

    public void validarSePersonagemEstaNaJornada(Personagem personagem, Integer jornadaId) {
        if (!personagem.getSessao().getJornada().getId().equals(jornadaId)) {
            throw new PersonagemNaoPertenceAJornadaException(personagem.getId(), jornadaId);
        }
    }

    public void validarSePersonagemAindaEstaJogando(Personagem personagem) {
        if (!PERSONAGEM_PODE_JOGAR.contains(personagem.getStatus())) {
            throw new PersonagemNaoEstaJogandoException(personagem.getId());
        }
    }

    public boolean validarSePersonagemFoiDerrotado(Personagem personagem) {
        return personagem.getPersonagemBarras()
                         .stream()
                         .anyMatch(personagemBarra -> personagemBarra.getValor() <= 0);
    }

    public PersonagemBarra buscarBarraQueCausouDerrotaDoPersonagem(Personagem personagem) {
        return personagem.getPersonagemBarras()
                         .stream()
                         .filter(personagemBarra -> personagemBarra.getValor() <= 0)
                         .findFirst()
                         .orElseThrow(() -> new RegistroNaoEncontradoException(""));
    }

    private List<PersonagemBarra> criarListaDeBarrasDoPersonagem(Personagem personagem) {
        List<Barra> barras = barraRepository.findAll();
        List<PersonagemBarra> personagemBarras = new ArrayList<>();
        barras.forEach(barra -> {
            var personagemBarra = new PersonagemBarra(obj -> {
                obj.setBarra(barra);
                obj.setPersonagem(personagem);
                obj.setValor(100);
            });
            personagemBarras.add(personagemBarra);
        });
        return personagemBarras;
    }

}
