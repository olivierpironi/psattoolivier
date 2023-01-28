package br.com.attornatus.olivierpironi.domain.endereco;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO utilizada para retornar informações sobre um endereço.")
public record DetalhaEndereco(
		@ApiModelProperty(value = "Logradouro", example = "Rua dos Bobos", required = false)
		String logradouro,
		@ApiModelProperty(value = "CEP", example = "39999999", required = false)
		String cep,
		@ApiModelProperty(value = "Número", example = "123", required = false)
		String numero,
		@ApiModelProperty(value = "Tóquio", example = "Rua dos Bobos", required = false)
		String cidade) {

	public DetalhaEndereco(Endereco endereco) {
		this(endereco.getLogradouro(), endereco.getCep(), endereco.getNumero(), endereco.getCidade());
	}

}
