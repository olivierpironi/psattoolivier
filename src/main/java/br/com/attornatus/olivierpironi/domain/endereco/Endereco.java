package br.com.attornatus.olivierpironi.domain.endereco;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.jpountz.xxhash.XXHashFactory;

@Entity(name = "endereco")
@Table(name = "enderecos")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Endereco {
	@Id
	private Integer id;
	private String logradouro;
	private String cep;
	private String numero;
	private String cidade;

	@PrePersist
	public void criarHash() {
		
		XXHashFactory factory = XXHashFactory.fastestInstance();
		this.id = factory.hash32().hash((logradouro + cep + numero + cidade).getBytes(), 0, 14, 0);
	}

	public Endereco(@Valid CadastroEndereco dados) {
		logradouro = dados.logradouro();
		cep = dados.cep();
		numero = dados.numero();
		cidade = dados.cidade();
	}

}
