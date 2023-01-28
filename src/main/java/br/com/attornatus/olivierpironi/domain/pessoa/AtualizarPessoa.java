package br.com.attornatus.olivierpironi.domain.pessoa;

import java.util.Optional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO utilizada para atualizar uma pessoa.")
public record AtualizarPessoa(
		@ApiModelProperty(value = "Nome", example = "Joana d'Arc", required = false)
		Optional<String> nome,
		@ApiModelProperty(value = "Data de Nascimento", example = "01/03/1412",required = false)
		Optional<String> dataNascimento) {
}

