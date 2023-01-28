package br.com.attornatus.olivierpironi.domain.endereco;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO utilizada para retornar informações sobre um endereço.")
public record CadastroEndereco(
		@ApiModelProperty(value = "Logradouro", example = "Rua dos Bobos", required = true)
		@NotBlank
		String logradouro,
		@ApiModelProperty(value = "CEP", example = "39999999", required = true)
		@NotBlank
		String cep,
		@ApiModelProperty(value = "Número", example = "123", required = true)
		@NotBlank
		String numero,
		@ApiModelProperty(value = "Tóquio", example = "Rua dos Bobos", required = true)
		@NotBlank
		String cidade) {

}

