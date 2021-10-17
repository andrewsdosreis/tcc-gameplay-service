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
public class ProximaJogadaDto {
    
    private Integer personagemId;
    private Integer jornadaId;
    private Integer jornadaCartaId;
    private Integer cartaId;
    private String cartaDescricao;
    private List<AlternativaDto> alternativas;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
