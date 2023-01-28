package br.com.attornatus.olivierpironi.domain.pessoa;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO utilizada para cadastrar uma pessoa.")
public record CadastroPessoa(
		@ApiModelProperty(value = "Nome", example = "Joana d'Arc", required = true)
		@NotBlank
		String nome,
		@ApiModelProperty(value = "Data de Nascimento", example = "01/03/1412",required = true)
		@NotBlank
		String dataNascimento,
		@ApiModelProperty(value = "Endereço Principal", required = true)
		@Valid
		CadastroEndereco enderecoPrincipal) {
}


