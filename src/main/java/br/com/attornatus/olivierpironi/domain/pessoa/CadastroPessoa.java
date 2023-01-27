package br.com.attornatus.olivierpironi.domain.pessoa;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;

public record CadastroPessoa(
		
		@NotBlank
		String nome,
		@NotBlank
		String dataNascimento,
		@Valid
		CadastroEndereco enderecoPrincipal) {
}
