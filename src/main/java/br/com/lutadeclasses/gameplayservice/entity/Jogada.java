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
@Table(name = "jogada", schema = "luta-de-classe-db")
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
