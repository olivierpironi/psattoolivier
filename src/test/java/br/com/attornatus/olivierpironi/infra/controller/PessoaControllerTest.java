package br.com.attornatus.olivierpironi.infra.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

	private static DadosParaTesteFactory p1;
	private static DadosParaTesteFactory p2;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		p1 = new DadosParaTesteFactory().criarPessoa1();
		p2 = new DadosParaTesteFactory().criarPessoa2();
	}
	
	@Test
	@DisplayName("Cadastrar pessoa com sucesso.")
	void criarPessoa_CadastroPessoa_ComSucesso() {
		// cen??rio
		when(pessoaService.cadastrar(p1.getCadastroPessoa())).thenReturn(p1.getPessoa());

		// a????o
		ResponseEntity<DetalhaPessoa> http = controller.criarPessoa(p1.getCadastroPessoa());

		// verifica????o
		assertThat(HttpStatus.CREATED, is( http.getStatusCode()));
		
		assertThat(p1.getDetalhaPessoa(), is( http.getBody()));
		
		assertThat(p1.getPessoa().getId().toString()).hasToString(http.getHeaders().getLocation().toString());
	}

	@Test
	@DisplayName("Cadastrar novo endereco em uma pessoa com sucesso.")
	void cadastrarEndereco_CadastrarEndereco_ComSucesso() {
		// cen??rio
		Long id = p1.getPessoa().getId();
		List<DetalhaEndereco> lista = p1.getPessoa().getListaDeEnderecos().stream().map(DetalhaEndereco::new).toList();
		when(pessoaService.cadastrarEndereco(id, p1.getCadastroEndereco())).thenReturn(lista);

		// a????o
		ResponseEntity<List<DetalhaEndereco>> http = controller.cadastrarEndereco(id, p1.getCadastroEndereco());

		// verifica????o
		assertThat(http.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		assertThat(lista).isEqualTo(http.getBody());
	}

	@Test
	@DisplayName("Atualizar endere??o principal de uma pessoa com sucesso.")
	void atualizarPessoa_AtualizarEndere??oPrincipal_ComSucesso() {
		// cen??rio
		Long idP1 = p1.getPessoa().getId();
		CadastroEndereco dados = new CadastroEndereco("end", "end", "end", "end");
		p1.getPessoa().setEnderecoPrincipal(new Endereco(dados));
		when(pessoaService.atualizarEnderecoPrincipal(idP1, dados)).thenReturn(new DetalhaPessoa(p1.getPessoa()));
		
		// a????o
		ResponseEntity<DetalhaPessoa> http = controller.atualizarEnderecoPrincipal(idP1, dados);
		
		// verifica????o
		assertThat(http.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		assertThat(new DetalhaPessoa(p1.getPessoa())).isEqualTo(http.getBody());
		
	}
	
	@Test
	@DisplayName("Atualizar pessoa pessoa com sucesso.")
	void atualizarPessoa_AtualizarPessoa_ComSucesso() {
		// cen??rio
		Long idP1 = p1.getPessoa().getId();
		AtualizarPessoa novosdados = p2.getDadosParaAtualizar(); 
		when(pessoaService.atualizar(idP1, novosdados)).thenReturn(p2.getDetalhaPessoa());

		// a????o
		ResponseEntity<DetalhaPessoa> http = controller.atualizarPessoa(idP1, novosdados);

		// verifica????o
		assertThat(http.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		assertThat(p2.getDetalhaPessoa()).isEqualTo(http.getBody());

	}
	

	@Test
	@DisplayName("Atualizar pessoa com endere??o n??o cadastrado.")
	void atualizarPessoa_AtualizarPessoa_ComEnderecoNaoCadastrado() {
		// cen??rio
		Long idP1 = p1.getPessoa().getId();
		AtualizarPessoa novosdados = p2.getDadosParaAtualizar(); 
		when(pessoaService.atualizar(idP1, novosdados)).thenThrow(new EnderecoNaoCadastradoException("Endere??o n??o cadastrado para o cliente."));

		//a????o
        Throwable e = Assertions.catchThrowable(() -> controller.atualizarPessoa(idP1, novosdados));

        //verifica????o
        assertThat(e).isInstanceOf(EnderecoNaoCadastradoException.class).hasMessageContaining("Endere??o n??o cadastrado para o cliente.");

	}

	@Test
	@DisplayName("Atualizar pessoa n??o cadastrada.")
	void atualizarPessoa_AtualizarPessoa_NaoCadastrada() {
		// cen??rio
		Long idP1 = p1.getPessoa().getId();
		AtualizarPessoa novosdados = p2.getDadosParaAtualizar();
		when(pessoaService.atualizar(idP1, novosdados)).thenThrow(new PessoaNaoEncontradaException("Pessoa com o ID especificado n??o encontrada"));

		// a????o
		Throwable e = Assertions.catchThrowable(() -> controller.atualizarPessoa(idP1, novosdados));
		
		// verifica????o
		assertThat(e).isInstanceOf(PessoaNaoEncontradaException.class).hasMessageContaining("Pessoa com o ID especificado n??o encontrada");

	}
	
	@Test
	@DisplayName("Consultar pessoa por ID.")
	void consultarById_ConsultarPessoaPorID_ComSucesso() {
		// cen??rio
		Long idP1 = p1.getPessoa().getId();
		when(pessoaService.consultarById(idP1)).thenReturn(p1.getDetalhaPessoa());

		// a????o
		ResponseEntity<DetalhaPessoa> http = controller.consultarById(idP1);

		// verifica????o
		assertThat(http.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		assertThat(p1.getDetalhaPessoa()).isEqualTo( http.getBody());
	}

	@Test
	@DisplayName("Listar todos os clientes.")
	void listarClientes() {
		// cen??rio
		List<DetalhaPessoa> lista = new ArrayList<DetalhaPessoa>();
		lista.add(p1.getDetalhaPessoa());
		lista.add(p2.getDetalhaPessoa());
		when(pessoaService.getListaClientes()).thenReturn(lista);

		// a????o
		ResponseEntity<List<DetalhaPessoa>> http = controller.listarClientes();

		// verifica????o
		assertThat(http.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		assertThat(lista).isEqualTo(http.getBody());
	}
	
	@Test
	@DisplayName("Paginar todos os clientes com sucesso.")
	void getPaginaClientes_PaginarClientes_ComSucesso() {
		// cen??rio
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(p1.getPessoa());
		lista.add(p2.getPessoa());
		Page<DetalhaPessoa> pagePessoa = new PageImpl<Pessoa>(lista).map(DetalhaPessoa::new);
		Pageable pageable = PageRequest.of(1, 1);
		when(pessoaService.getPaginaClientes(pageable)).thenReturn(pagePessoa);
		
		// a????o
		ResponseEntity<Page<DetalhaPessoa>> http = controller.paginasClientes(pageable);
		
		// verifica????o
		assertThat(http.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		assertThat(pagePessoa).isEqualTo(http.getBody());
	}
}
