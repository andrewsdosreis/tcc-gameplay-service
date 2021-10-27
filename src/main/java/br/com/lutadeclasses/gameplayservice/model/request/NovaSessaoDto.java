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
public class NovaSessaoDto {
    @NotBlank(message = "O campo titulo não pode ser vazio")
    private String titulo;

    @NotNull(message = "O campo jornadaId não pode ser nulo")
    private Integer jornadaId;
}