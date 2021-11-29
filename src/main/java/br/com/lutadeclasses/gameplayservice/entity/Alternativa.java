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
@Table(name = "alternativa")
public class Alternativa {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;
    
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "carta_id", nullable = false)
    @JsonBackReference
    private Carta carta;

    @OneToMany(mappedBy = "alternativa")
    @JsonManagedReference
    private List<Acao> acoes;

    public Alternativa() {
    }

    public Alternativa(Consumer<Alternativa> alternativa) {
        alternativa.accept(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public List<Acao> getAcoes() {
        return acoes;
    }

    public void setAcoes(List<Acao> acaoList) {
        this.acoes = acaoList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Alternativa other = (Alternativa) obj;
        if (acoes == null) {
            if (other.acoes != null)
                return false;
        } else if (!acoes.equals(other.acoes))
            return false;
        if (carta == null) {
            if (other.carta != null)
                return false;
        } else if (!carta.equals(other.carta))
            return false;
        if (descricao == null) {
            if (other.descricao != null)
                return false;
        } else if (!descricao.equals(other.descricao))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
