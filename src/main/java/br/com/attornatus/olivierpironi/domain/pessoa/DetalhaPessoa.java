package br.com.attornatus.olivierpironi.domain.pessoa;

import java.util.List;

import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description = "DTO utilizada para retornar informações sobre uma pessoa.")
public record DetalhaPessoa(
		@ApiModelProperty(value = "Nome", example = "Joana d'Arc", required = false)
		String nome, 
		@ApiModelProperty(value = "Data de Nascimento", example = "01/03/1412",required = false)
		String dataDeNascimento, 
		@ApiModelProperty(value = "Endereço Principal", required = false)
		DetalhaEndereco enderecoPrincipal,
		@ApiModelProperty(value = "Lista de Endereços",  required = false)
		List<DetalhaEndereco> listaEnderecos) {

	public DetalhaPessoa(Pessoa pessoa) {
		this(
				pessoa.getNome(), pessoa.getDataNascimento().toString(),

				new DetalhaEndereco(pessoa.getEnderecoPrincipal()),

				pessoa.getListaDeEnderecos().stream().map(DetalhaEndereco::new).toList());

	}
}
