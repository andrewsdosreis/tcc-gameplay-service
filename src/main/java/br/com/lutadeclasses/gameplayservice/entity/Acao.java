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
@Table(name = "acao")
public class Acao {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "tipo", updatable = false, nullable = false)
    private String tipo;

    @Column(name = "valor", updatable = false, nullable = false)
    private Integer valor;

    @ManyToOne
    @JoinColumn(name = "alternativa_id", nullable = false)
    @JsonBackReference
    private Alternativa alternativa;

    @ManyToOne
    @JoinColumn(name = "barra_id", nullable = false)
    @JsonBackReference
    private Barra barra;

    public Acao() {
    }

    public Acao(Consumer<Acao> acao) {
        acao.accept(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
    }

    public Barra getBarra() {
        return barra;
    }

    public void setBarra(Barra barra) {
        this.barra = barra;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Acao other = (Acao) obj;
        if (alternativa == null) {
            if (other.alternativa != null)
                return false;
        } else if (!alternativa.equals(other.alternativa))
            return false;
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
        if (tipo == null) {
            if (other.tipo != null)
                return false;
        } else if (!tipo.equals(other.tipo))
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
