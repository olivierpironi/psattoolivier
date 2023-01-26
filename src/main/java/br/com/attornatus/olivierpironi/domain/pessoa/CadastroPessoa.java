package br.com.attornatus.olivierpironi.domain.pessoa;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroPessoa(
		
		@NotBlank
		String nome,
		@NotBlank
		String dataNascimento,
		@NotNull
		CadastroEndereco enderecoPrincipal) {
}
