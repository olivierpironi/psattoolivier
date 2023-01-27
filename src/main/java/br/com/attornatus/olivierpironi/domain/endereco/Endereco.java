package br.com.attornatus.olivierpironi.domain.endereco;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "endereco")
@Table(name = "enderecos")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, exclude = "id")
public class Endereco {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String logradouro;
	private String cep;
	private String numero;
	private String cidade;


	
	public Endereco(@Valid CadastroEndereco dados) {
		logradouro = dados.logradouro();
		cep = dados.cep();
		numero = dados.numero();
		cidade = dados.cidade();
	}

}
