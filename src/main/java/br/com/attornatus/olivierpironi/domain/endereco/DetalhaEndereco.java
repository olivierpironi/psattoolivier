package br.com.attornatus.olivierpironi.domain.endereco;


public record DetalhaEndereco(
		
		String logradouro,
		
		String cep,
		
		String numero,
		
		String cidade) {

	public DetalhaEndereco(Endereco endereco) {
		this(endereco.getLogradouro(), endereco.getCep(), endereco.getNumero(), endereco.getCidade());
	}

}
