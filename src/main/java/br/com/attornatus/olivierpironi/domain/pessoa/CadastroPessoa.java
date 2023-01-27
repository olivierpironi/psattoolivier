package br.com.attornatus.olivierpironi.domain.pessoa;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record CadastroPessoa(
		
		@NotBlank
		String nome,
		@NotBlank
		String dataNascimento,
		@Valid
		CadastroEndereco enderecoPrincipal) {
}
