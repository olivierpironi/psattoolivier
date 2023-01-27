package br.com.attornatus.olivierpironi.infra.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.attornatus.olivierpironi.domain.pessoa.Pessoa;
import br.com.attornatus.olivierpironi.util.DadosParaTesteFactory;

@DataJpaTest
@DisplayName("Teste para o repositório de pessoas.")
class PessoaRepositoryTest {

	@Autowired
	private PessoaRepository repository;
	
	DadosParaTesteFactory factory = new DadosParaTesteFactory();
	
	@Test
	@DisplayName("Persistencia com sucesso.")
	void save_Persistencia_ComSucesso() {
		// ação
		Pessoa pessoa = factory.criarPessoa();
		Pessoa pessoaSalvada = repository.save(pessoa);
		
		// verificação
		assertThat(pessoaSalvada).isNotNull();
		
		assertThat(pessoaSalvada.getId()).isNotNull();
		
		assertThat(pessoaSalvada.getNome()).isEqualTo(pessoa.getNome());
	}
	
	@Test
	@DisplayName("Atualizar pessoa com sucesso.")
	void update_Atualizar_ComSucesso() {
		// cenário
		Pessoa pessoaSalvada = repository.save(factory.criarPessoa());
		
		// ação
		pessoaSalvada.setNome("Josias");
		
		Pessoa pessoaAtualizada = repository.save(pessoaSalvada);
		
		// verificação
		assertThat(pessoaAtualizada).isNotNull();
		
		assertThat(pessoaAtualizada.getId()).isEqualTo(pessoaSalvada.getId());
		
		assertThat(pessoaAtualizada.getNome()).isEqualTo(pessoaSalvada.getNome());
	}
	
	@Test
	@DisplayName("Buscar pessoa por ID com sucesso.")
	void findById_BuscarPorId_ComSucesso() {
		// cenário
		Pessoa pessoaSalvada = repository.save(factory.criarPessoa());
		
		// ação
		Pessoa pessoaBuscada = repository.findById(pessoaSalvada.getId()).get();
		
		
		// verificação
		assertThat(pessoaBuscada).isNotNull();
		
		assertThat(pessoaBuscada.getId()).isEqualTo(pessoaSalvada.getId());
	}
	
	@Test
	@DisplayName("Buscar pessoa por ID que não existe.")
	void findById_BuscarPorId_NaoExiste() {
		// ação
        Throwable e = Assertions.catchThrowable(() -> repository.findById(0l).get());

        // verificação
        assertThat(e).isInstanceOf(NoSuchElementException.class).hasMessageContaining("No value present");
	}
	
	@Test
	@DisplayName("Buscar todos os cadastros de pessoa com sucesso.")
	void findAll_BuscarTodosCadastros_ComSucesso() {
		// cenário
		Pessoa pessoaSalvada1 = repository.save(factory.criarPessoa());
		Pessoa pessoaSalvada2 = repository.save(factory.criarPessoa());
		
		ArrayList<Pessoa> listaResultado = new ArrayList<>();
		listaResultado.add(pessoaSalvada1);
		listaResultado.add(pessoaSalvada2);
		
		// ação
		List<Pessoa> lista = repository.findAll();
		
		// verificação
	 assertThat(lista).isEqualTo(listaResultado);
	}
	
	@Test
	@DisplayName("Buscar todos os cadastros de pessoa quando não há pessoas cadastradas.")
	void findAll_BuscarTodosCadastros_SemPessoasCadastradas() {
		// ação
		List<Pessoa> lista = repository.findAll();
		
		// verificação
		assertThat(lista).isEmpty();
	}

}
