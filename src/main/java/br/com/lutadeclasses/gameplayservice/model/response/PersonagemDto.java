package br.com.lutadeclasses.gameplayservice.model.response;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PersonagemDto {
    private Integer usuarioId;
    private String usuarioUsername;
    private Integer sessaoId;
    private String sessaoTitulo;
    private Integer personagemId;
    private String personagemNome;
    private String personagemStatus;
    private List<PersonagemBarraDto> barras;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}