package br.com.attornatus.olivierpironi.domain.endereco;


import jakarta.validation.constraints.NotBlank;

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
