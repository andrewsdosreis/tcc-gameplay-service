package br.com.lutadeclasses.gameplayservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.Acao;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;
import br.com.lutadeclasses.gameplayservice.exception.notfound.PersonagemNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.notfound.RegistroNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.validation.PersonagemNaoEstaJogandoException;
import br.com.lutadeclasses.gameplayservice.exception.validation.PersonagemNaoPertenceAJornadaException;
import br.com.lutadeclasses.gameplayservice.model.AcaoTipoEnum;
import br.com.lutadeclasses.gameplayservice.model.PersonagemStatusEnum;
import br.com.lutadeclasses.gameplayservice.repository.PersonagemBarraRepository;
import br.com.lutadeclasses.gameplayservice.repository.PersonagemRepository;

@Service
public class PersonagemService {
    
    private PersonagemRepository personagemRepository;
    private PersonagemBarraRepository personagemBarraRepository;
    
    public PersonagemService(PersonagemRepository personagemRepository, PersonagemBarraRepository personagemBarraRepository) {
        this.personagemRepository = personagemRepository;
        this.personagemBarraRepository = personagemBarraRepository;
    }

    public Personagem buscarPersonagem(Integer personagemId) {
        return personagemRepository.findById(personagemId)
                                   .orElseThrow(() -> new PersonagemNaoEncontradoException(personagemId));
    }

    public void validarSePersonagemEstaNaJornada(Personagem personagem, Integer jornadaId) {
        if (!personagem.getSessao().getJornada().getId().equals(jornadaId)) {
            throw new PersonagemNaoPertenceAJornadaException(personagem.getId(), jornadaId);
        }
    }

    public void validarSePersonagemAindaEstaJogando(Personagem personagem) {
        if (!personagem.getStatus().equals(PersonagemStatusEnum.JOGANDO.toString())) {
            throw new PersonagemNaoEstaJogandoException(personagem.getId());
        }
    }

    public boolean verificarSePersonagemFoiDerrotado(Personagem personagem) {
        return personagem.getPersonagemBarraList()
                         .stream()
                         .anyMatch(personagemBarra -> personagemBarra.getValor() <= 0);
    }

    public PersonagemBarra buscarBarraQueCausouDerrotaDoPersonagem(Personagem personagem) {
        return personagem.getPersonagemBarraList()
                         .stream()
                         .filter(personagemBarra -> personagemBarra.getValor() <= 0)
                         .findFirst()
                         .orElseThrow(() -> new RegistroNaoEncontradoException(""));
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
                    }
                }
            });

        return personagemBarraRepository.saveAll(personagemBarraList);
    }

}
