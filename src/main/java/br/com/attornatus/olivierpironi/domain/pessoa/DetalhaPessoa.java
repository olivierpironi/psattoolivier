package br.com.attornatus.olivierpironi.domain.pessoa;

import java.util.List;

import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;

public record DetalhaPessoa(
		String nome, 
		
		String dataDeNascimento, 
		
		DetalhaEndereco enderecoPrincipal,
		
		List<DetalhaEndereco> listaEnderecos) {

	public DetalhaPessoa(Pessoa pessoa) {
		this(
				pessoa.getNome(), pessoa.getDataNascimento().toString(),

				new DetalhaEndereco(pessoa.getEnderecoPrincipal()),

				pessoa.getListaDeEnderecos().stream().map(DetalhaEndereco::new).toList());

	}
}
