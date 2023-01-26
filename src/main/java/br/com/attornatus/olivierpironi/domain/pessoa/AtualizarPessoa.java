package br.com.attornatus.olivierpironi.domain.pessoa;

import java.util.Optional;

public record AtualizarPessoa(
		
		Optional<String> nome,
		
		Optional<String> dataNascimento) {
}
