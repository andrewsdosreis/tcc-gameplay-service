package br.com.lutadeclasses.gameplayservice.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NovoPersonagemDto {
    @NotNull(message = "O campo 'usuarioId' não pode ser nulo")
    private Integer usuarioId;
    
    @NotNull(message = "O campo 'sessaoId' não pode ser nulo")
    private Integer sessaoId;
    
    @NotBlank(message = "O campo 'nome' não pode ser vazio")
    private String nome;
}