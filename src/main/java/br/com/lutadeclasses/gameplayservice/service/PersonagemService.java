package br.com.lutadeclasses.gameplayservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.lutadeclasses.gameplayservice.entity.Acao;
import br.com.lutadeclasses.gameplayservice.entity.Personagem;
import br.com.lutadeclasses.gameplayservice.entity.PersonagemBarra;
import br.com.lutadeclasses.gameplayservice.exception.GameOverException;
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

    public Optional<Personagem> buscarPersonagem(Integer personagemId) {
        return Optional.ofNullable(personagemRepository.getById(personagemId));
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

    public void verificarSePersonagemFoiDerrotado(Personagem personagem) {
        if (personagem.getPersonagemBarraList().stream().anyMatch(barra -> barra.getValor() <= 0)) {
            personagem.setStatus(PersonagemStatusEnum.DERROTADO.toString());
            personagemRepository.save(personagem);
            throw new GameOverException();
        }
    }

    public void atualizarPersonagemParaVencedor(Personagem personagem) {
        personagem.setStatus(PersonagemStatusEnum.VENCEDOR.toString());
        personagemRepository.save(personagem);
    }

}
