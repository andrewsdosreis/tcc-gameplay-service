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
@Table(name = "jornada_carta")
public class JornadaCarta {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "jornada_id", nullable = false)
    @JsonBackReference
    private Jornada jornada;

    @ManyToOne
    @JoinColumn(name = "carta_id", nullable = false)
    @JsonBackReference
    private Carta carta;

    @Column(name = "posicao", nullable = false)
    private String posicao;

    @OneToMany(mappedBy = "jornadaCarta")
    @JsonManagedReference
    private List<JornadaAlternativa> jornadaAlternativaList;

    public JornadaCarta() {
    }

    public JornadaCarta(Consumer<JornadaCarta> jornadaCarta) {
        jornadaCarta.accept(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public List<JornadaAlternativa> getJornadaAlternativaList() {
        return jornadaAlternativaList;
    }

    public void setJornadaAlternativaList(List<JornadaAlternativa> jornadaAlternativaList) {
        this.jornadaAlternativaList = jornadaAlternativaList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JornadaCarta other = (JornadaCarta) obj;
        if (carta == null) {
            if (other.carta != null)
                return false;
        } else if (!carta.equals(other.carta))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jornada == null) {
            if (other.jornada != null)
                return false;
        } else if (!jornada.equals(other.jornada))
            return false;
        if (jornadaAlternativaList == null) {
            if (other.jornadaAlternativaList != null)
                return false;
        } else if (!jornadaAlternativaList.equals(other.jornadaAlternativaList))
            return false;
        if (posicao == null) {
            if (other.posicao != null)
                return false;
        } else if (!posicao.equals(other.posicao))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
