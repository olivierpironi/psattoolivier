package br.com.attornatus.olivierpironi.domain.endereco;


import javax.validation.constraints.NotBlank;

public record CadastroEndereco(
		
		@NotBlank
		String logradouro,
		@NotBlank
		String cep,
		@NotBlank
		String numero,
		@NotBlank
		String cidade) {

}
