package br.com.lutadeclasses.gameplayservice.entity;

import java.util.List;
import java.util.function.Consumer;

import javax.persistence.CascadeType;
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
@Table(name = "personagem")
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
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    @JsonBackReference
    private Sessao sessao;

    @OneToMany(mappedBy = "personagem", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PersonagemBarra> personagemBarras;

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public List<PersonagemBarra> getPersonagemBarras() {
        return personagemBarras;
    }

    public void setPersonagemBarras(List<PersonagemBarra> personagemBarraList) {
        this.personagemBarras = personagemBarraList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Personagem other = (Personagem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (personagemBarras == null) {
            if (other.personagemBarras != null)
                return false;
        } else if (!personagemBarras.equals(other.personagemBarras))
            return false;
        if (sessao == null) {
            if (other.sessao != null)
                return false;
        } else if (!sessao.equals(other.sessao))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (usuario == null) {
            if (other.usuario != null)
                return false;
        } else if (!usuario.equals(other.usuario))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
