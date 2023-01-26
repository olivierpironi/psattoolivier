package br.com.attornatus.olivierpironi.domain.pessoa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.com.attornatus.olivierpironi.domain.endereco.Endereco;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
	@OneToOne(cascade = CascadeType.PERSIST)
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
