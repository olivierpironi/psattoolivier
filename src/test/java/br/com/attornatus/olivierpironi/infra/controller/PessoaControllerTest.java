package br.com.attornatus.olivierpironi.infra.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.Endereco;
import br.com.attornatus.olivierpironi.domain.exception.EnderecoNaoCadastradoException;
import br.com.attornatus.olivierpironi.domain.exception.PessoaNaoEncontradaException;
import br.com.attornatus.olivierpironi.domain.pessoa.AtualizarPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.DetalhaPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.Pessoa;
import br.com.attornatus.olivierpironi.domain.service.PessoaService;
import br.com.attornatus.olivierpironi.util.DadosParaTesteFactory;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste para o controller de pessoas.")
class PessoaControllerTest {

	@InjectMocks
	private PessoaController controller;
	@Mock
	private static PessoaService pessoaService;

	private static DadosParaTesteFactory factory = new DadosParaTesteFactory();
	private static DadosParaTesteFactory p1;
	private static DadosParaTesteFactory p2;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		p1 = factory.criarPessoa1();
		p2 = factory.criarPessoa2();
	}
	
	@Test
	@DisplayName("Cadastrar pessoa com sucesso.")
	void criarPessoa_CadastroPessoa_ComSucesso() {
		// cenário
		when(pessoaService.cadastrar(p1.getCadastroPessoa())).thenReturn(p1.getPessoa());

		// ação
		ResponseEntity<DetalhaPessoa> http = controller.criarPessoa(p1.getCadastroPessoa());

		// verificação
		assertThat(HttpStatus.CREATED, is( http.getStatusCode()));
		
		assertThat(p1.getDetalhaPessoa(), is( http.getBody()));
		
		assertThat(p1.getPessoa().getId().toString()).hasToString(http.getHeaders().getLocation().toString()); //TODO MELHORAR ASSERTS
	}

	@Test
	@DisplayName("Cadastrar novo endereco em uma pessoa com sucesso.")
	void cadastrarEndereco_CadastrarEndereco_ComSucesso() {
		// cenário
		Long id = p1.getPessoa().getId();
		List<DetalhaEndereco> lista = p1.getPessoa().getListaDeEnderecos().stream().map(DetalhaEndereco::new).toList();
		when(pessoaService.cadastrarEndereco(id, p1.getCadastroEndereco())).thenReturn(lista);

		// ação
		ResponseEntity<List<DetalhaEndereco>> http = controller.cadastrarEndereco(id, p1.getCadastroEndereco());

		// verificação
		assertThat(HttpStatus.CREATED, is(http.getStatusCode()));
		
		assertThat(lista, is(http.getBody()));
	}

	@Test
	@DisplayName("Atualizar pessoa pessoa com sucesso.")
	void atualizarPessoa_AtualizarPessoa_ComSucesso() {
		// cenário
		Long idP1 = p1.getPessoa().getId();
		AtualizarPessoa novosdados = new AtualizarPessoa(Optional.of("Joana"), Optional.of("03/03/1999")); //TODO atualizar isso e também os asserts
		when(pessoaService.atualizar(idP1, novosdados)).thenReturn(p2.getDetalhaPessoa());

		// ação
		ResponseEntity<DetalhaPessoa> http = controller.atualizarPessoa(idP1, novosdados);

		// verificação
		assertThat(HttpStatus.OK, is(http.getStatusCode()));
		assertThat(p2.getDetalhaPessoa(), is(http.getBody()));

	}
	
	@Test
	@DisplayName("Atualizar endereço principal de uma pessoa com sucesso.")
	void atualizarPessoa_AtualizarEndereçoPrincipal_ComSucesso() {
		// cenário
		Long idP1 = p1.getPessoa().getId();
		CadastroEndereco dados = new CadastroEndereco("end", "end", "end", "end");
		p1.getPessoa().setEnderecoPrincipal(new Endereco(dados));
		when(pessoaService.atualizarEnderecoPrincipal(idP1, dados)).thenReturn(new DetalhaPessoa(p1.getPessoa()));
		
		// ação
		ResponseEntity<DetalhaPessoa> http = controller.atualizarEnderecoPrincipal(idP1, dados);
		
		// verificação
		assertThat(HttpStatus.OK, is(http.getStatusCode()));
		assertThat(new DetalhaPessoa(p1.getPessoa()), is(http.getBody()));
		
	}

	@Test
	@DisplayName("Atualizar pessoa com endereço não cadastrado.")
	void atualizarPessoa_AtualizarPessoa_ComEnderecoNaoCadastrado() {
		// cenário
		Long idP1 = p1.getPessoa().getId();
		AtualizarPessoa novosdados = new AtualizarPessoa(Optional.of("Joana"), Optional.of("03/03/1999"));
		when(pessoaService.atualizar(idP1, novosdados)).thenThrow(new EnderecoNaoCadastradoException("Endereço não cadastrado para o cliente."));

		//ação
        Throwable e = Assertions.catchThrowable(() -> controller.atualizarPessoa(idP1, novosdados));

        //verificação
        assertThat(e).isInstanceOf(EnderecoNaoCadastradoException.class).hasMessageContaining("Endereço não cadastrado para o cliente.");

	}

	@Test
	@DisplayName("Atualizar pessoa não cadastrada.")
	void atualizarPessoa_AtualizarPessoa_NaoCadastrada() {
		// cenário
		Long idP1 = p1.getPessoa().getId();
		AtualizarPessoa novosdados = new AtualizarPessoa(Optional.of("Joana"), Optional.of("03/03/1999"));
		when(pessoaService.atualizar(idP1, novosdados)).thenThrow(new PessoaNaoEncontradaException("Pessoa com o ID especificado não encontrada"));

		// ação
		Throwable e = Assertions.catchThrowable(() -> controller.atualizarPessoa(idP1, novosdados));
		
		// verificação
		assertThat(e).isInstanceOf(PessoaNaoEncontradaException.class).hasMessageContaining("Pessoa com o ID especificado não encontrada");

	}
	
	@Test
	@DisplayName("Consultar pessoa por ID.")
	void consultarById_ConsultarPessoaPorID_ComSucesso() {
		// cenário
		Long idP1 = p1.getPessoa().getId();
		when(pessoaService.consultarById(idP1)).thenReturn(p1.getDetalhaPessoa());

		// ação
		ResponseEntity<DetalhaPessoa> http = controller.consultarById(idP1);

		// verificação
		assertThat(HttpStatus.OK, is(http.getStatusCode()));
		
		assertThat(p1.getDetalhaPessoa(), is( http.getBody()));
	}

	@Test
	@DisplayName("Listar todos os clientes.")
	void listarClientes() {
		// cenário
		List<DetalhaPessoa> lista = new ArrayList<DetalhaPessoa>();
		lista.add(p1.getDetalhaPessoa());
		lista.add(p2.getDetalhaPessoa());
		when(pessoaService.getListaClientes()).thenReturn(lista);

		// ação
		ResponseEntity<List<DetalhaPessoa>> http = controller.listarClientes();

		// verificação
		assertThat(HttpStatus.OK, is(http.getStatusCode()));
		
		assertThat(lista, is(http.getBody()));
	}
	
	@Test
	@DisplayName("Paginar todos os clientes com sucesso.")
	void getPaginaClientes_PaginarClientes_ComSucesso() {
		// cenário
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(p1.getPessoa());
		lista.add(p2.getPessoa());
		Page<DetalhaPessoa> pagePessoa = new PageImpl<Pessoa>(lista).map(DetalhaPessoa::new);
		Pageable pageable = PageRequest.of(1, 1);
		when(pessoaService.getPaginaClientes(pageable)).thenReturn(pagePessoa);
		
		// ação
		ResponseEntity<Page<DetalhaPessoa>> http = controller.paginasClientes(pageable);
		
		// verificação
		assertThat(HttpStatus.OK, is(http.getStatusCode()));
		
		assertThat(pagePessoa, is(http.getBody()));
	}
}
