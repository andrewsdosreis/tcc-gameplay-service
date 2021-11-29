package br.com.lutadeclasses.gameplayservice.entity;

import java.time.LocalDateTime;
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
@Table(name = "jogada")
public class Jogada {
    
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personagem_id", nullable = false)
    @JsonBackReference
    private Personagem personagem;

    @ManyToOne
    @JoinColumn(name = "jornada_carta_id", nullable = false)
    @JsonBackReference
    private JornadaCarta jornadaCarta;

    @ManyToOne
    @JoinColumn(name = "jornada_alternativa_id", nullable = false)
    @JsonBackReference
    private JornadaAlternativa jornadaAlternativa;

    @Column(name = "dataHora", nullable = false)
    private LocalDateTime dataHora;

    public Jogada() {
    }

    public Jogada(Consumer<Jogada> jogada) {
        jogada.accept(this);
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

    public JornadaCarta getJornadaCarta() {
        return jornadaCarta;
    }

    public void setJornadaCarta(JornadaCarta jornadaCarta) {
        this.jornadaCarta = jornadaCarta;
    }

    public JornadaAlternativa getJornadaAlternativa() {
        return jornadaAlternativa;
    }

    public void setJornadaAlternativa(JornadaAlternativa jornadaAlternativa) {
        this.jornadaAlternativa = jornadaAlternativa;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Jogada other = (Jogada) obj;
        if (dataHora == null) {
            if (other.dataHora != null)
                return false;
        } else if (!dataHora.equals(other.dataHora))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (jornadaAlternativa == null) {
            if (other.jornadaAlternativa != null)
                return false;
        } else if (!jornadaAlternativa.equals(other.jornadaAlternativa))
            return false;
        if (jornadaCarta == null) {
            if (other.jornadaCarta != null)
                return false;
        } else if (!jornadaCarta.equals(other.jornadaCarta))
            return false;
        if (personagem == null) {
            if (other.personagem != null)
                return false;
        } else if (!personagem.equals(other.personagem))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
