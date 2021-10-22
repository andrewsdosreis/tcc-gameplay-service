package br.com.lutadeclasses.gameplayservice.entity;

import java.util.List;
import java.util.function.Consumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "personagem", schema = "luta-de-classe-db")
public class Personagem {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    @JsonBackReference
    private Sessao sessao;

    @OneToMany(mappedBy = "personagem")
    @JsonManagedReference
    private List<PersonagemBarra> personagemBarraList;

    public Personagem() {
    }
    
    public Personagem(Consumer<Personagem> personagem) {
        personagem.accept(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public List<PersonagemBarra> getPersonagemBarraList() {
        return personagemBarraList;
    }

    public void setPersonagemBarraList(List<PersonagemBarra> personagemBarraList) {
        this.personagemBarraList = personagemBarraList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
