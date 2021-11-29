package br.com.lutadeclasses.gameplayservice.entity;

import java.util.function.Consumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "personagem_barra")
public class PersonagemBarra {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personagem_id", nullable = false)
    @JsonBackReference
    private Personagem personagem;

    @ManyToOne
    @JoinColumn(name = "barra_id", nullable = false)
    @JsonBackReference
    private Barra barra;

    @Column(name = "valor", nullable = false)
    private Integer valor;

    public PersonagemBarra() {
    }

    public PersonagemBarra(Consumer<PersonagemBarra> personagemBarra) {
        personagemBarra.accept(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }

    public Barra getBarra() {
        return barra;
    }

    public void setBarra(Barra barra) {
        this.barra = barra;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonagemBarra other = (PersonagemBarra) obj;
        if (barra == null) {
            if (other.barra != null)
                return false;
        } else if (!barra.equals(other.barra))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (personagem == null) {
            if (other.personagem != null)
                return false;
        } else if (!personagem.equals(other.personagem))
            return false;
        if (valor == null) {
            if (other.valor != null)
                return false;
        } else if (!valor.equals(other.valor))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
