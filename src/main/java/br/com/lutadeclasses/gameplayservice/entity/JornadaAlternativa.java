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
@Table(name = "jornada_alternativa")
public class JornadaAlternativa {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "jornada_carta_id", nullable = false)
    @JsonBackReference
    private JornadaCarta jornadaCarta;

    @ManyToOne
    @JoinColumn(name = "proxima_jornada_carta_id", nullable = true)
    @JsonBackReference
    private JornadaCarta proximaJornadaCarta;

    @ManyToOne
    @JoinColumn(name = "alternativa_id", nullable = false)
    @JsonBackReference
    private Alternativa alternativa;

    public JornadaAlternativa() {
    }

    public JornadaAlternativa(Consumer<JornadaAlternativa> jornadaAlternativa) {
        jornadaAlternativa.accept(this);
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JornadaCarta getJornadaCarta() {
        return jornadaCarta;
    }

    public void setJornadaCarta(JornadaCarta jornadaCarta) {
        this.jornadaCarta = jornadaCarta;
    }

    public JornadaCarta getProximaJornadaCarta() {
        return proximaJornadaCarta;
    }

    public void setProximaJornadaCarta(JornadaCarta proximaJornadaCarta) {
        this.proximaJornadaCarta = proximaJornadaCarta;
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JornadaAlternativa other = (JornadaAlternativa) obj;
        if (alternativa == null) {
            if (other.alternativa != null)
                return false;
        } else if (!alternativa.equals(other.alternativa))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jornadaCarta == null) {
            if (other.jornadaCarta != null)
                return false;
        } else if (!jornadaCarta.equals(other.jornadaCarta))
            return false;
        if (proximaJornadaCarta == null) {
            if (other.proximaJornadaCarta != null)
                return false;
        } else if (!proximaJornadaCarta.equals(other.proximaJornadaCarta))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
