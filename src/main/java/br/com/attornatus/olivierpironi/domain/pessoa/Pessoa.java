package br.com.attornatus.olivierpironi.domain.pessoa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.attornatus.olivierpironi.domain.endereco.Endereco;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "pessoa")
@Table(name = "pessoas")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Pessoa {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String nome;
	private LocalDate dataNascimento;
	@OneToOne(cascade = CascadeType.ALL)
	private Endereco enderecoPrincipal;
	@OneToMany(cascade = CascadeType.ALL)
	@ElementCollection(targetClass = Endereco.class)
	private List<Endereco> listaDeEnderecos = new ArrayList<>();

	public Pessoa(CadastroPessoa dados) {
		nome = dados.nome();
		dataNascimento = LocalDate.parse(dados.dataNascimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		enderecoPrincipal = new Endereco(dados.enderecoPrincipal());
		listaDeEnderecos.add(enderecoPrincipal);
	}

}
