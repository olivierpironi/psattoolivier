package br.com.attornatus.olivierpironi.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.Endereco;
import br.com.attornatus.olivierpironi.domain.exception.EnderecoNaoCadastradoException;
import br.com.attornatus.olivierpironi.domain.exception.EntidadeExisteException;
import br.com.attornatus.olivierpironi.domain.pessoa.AtualizarPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.DetalhaPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.Pessoa;
import br.com.attornatus.olivierpironi.infra.repository.PessoaRepository;
import br.com.attornatus.olivierpironi.util.DadosParaTesteFactory;

@ExtendWith(SpringExtension.class)
@DisplayName("Testes para o serviço de pessoas")
class PessoaServiceTest {
	
	@InjectMocks
	private PessoaService pessoaService;
	@Mock
	private PessoaRepository pessoaRepository;
	
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
	void cadastrar_CadastroPessoa_ComSucesso() {
		// cenário
		when(pessoaRepository.save(p1.getPessoa())).thenReturn(p1.getPessoa());
		
		// ação
		Pessoa pessoa = pessoaService.cadastrar(p1.getCadastroPessoa());

		//verificação
		assertThat(pessoa).isNotNull();
				
		assertThat(pessoa.getNome()).isEqualTo(p1.getCadastroPessoa().nome());
	}

	@Test
	@DisplayName("Cadastrar novo endereco em uma pessoa com sucesso.")
	void cadastrarEndereco_CadastrarEndereco_ComSucesso() {
		// cenário
		Long id = p1.getPessoa().getId();
		CadastroEndereco novosDados = new CadastroEndereco("end", "end", "end", "end");
		Endereco novoEndereco = new Endereco(novosDados);
		when(pessoaRepository.findById(ArgumentMatchers.anyLong()))
				.thenReturn(Optional.of(p1.getPessoa()));
		// ação
		List<DetalhaEndereco> endereco = pessoaService.cadastrarEndereco(id, novosDados);
		
		// verificação
		assertThat(endereco).contains(new DetalhaEndereco(novoEndereco));
	}
	
	@Test
	@DisplayName("Cadastrar novo endereco duplicado.")
	void cadastrarEndereco_CadastrarEndereco_Duplicado() {
		// cenário
		Pessoa pessoa = p1.getPessoa();
		Long id = pessoa.getId();
		
		CadastroEndereco novoEndereco = new CadastroEndereco("end", "end", "end", "end");
		pessoa.getListaDeEnderecos().add(new Endereco(novoEndereco));
		
		when(pessoaRepository.findById(ArgumentMatchers.anyLong()))
		.thenReturn(Optional.of(pessoa));
		// ação
        Throwable e = Assertions.catchThrowable(() -> pessoaService.cadastrarEndereco(id, novoEndereco));

        // verificação
        assertThat(e).isInstanceOf(EntidadeExisteException.class).hasMessageContaining("Endereço já cadastrado para este cliente.");
	}
	
	
	@Test
	@DisplayName("Atualizar endereco principal em uma pessoa com sucesso.")
	void atualizarEnderecoPrincipal_AtualizarEndereco_ComSucesso() {
		// cenário
		Pessoa pessoa = p1.getPessoa();
		Long id = pessoa.getId();
		CadastroEndereco dados = new CadastroEndereco("end", "end", "end", "end");
		Endereco endereco = new Endereco(dados);
		when(pessoaRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(pessoa));
		pessoa.getListaDeEnderecos().add(endereco);
		// ação
		DetalhaPessoa atualizarEnderecoPrincipal = pessoaService.atualizarEnderecoPrincipal(id, dados);
		
		// verificação
		assertThat(atualizarEnderecoPrincipal).isEqualTo(new DetalhaPessoa(pessoa));
	}
	
	@Test
	@DisplayName("Atualizar endereco principal em uma pessoa com sucesso.")
	void atualizarEnderecoPrincipal_AtualizarEndereco_EnderecoNaoCadastrado() {
		// cenário
		Pessoa pessoa = p1.getPessoa();
		Long id = pessoa.getId();
		CadastroEndereco dados = new CadastroEndereco("end", "end", "end", "end");
		when(pessoaRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(pessoa));
		//ação
        Throwable e = Assertions.catchThrowable(() -> pessoaService.atualizarEnderecoPrincipal(id, dados));

        //verificação
        assertThat(e).isInstanceOf(EnderecoNaoCadastradoException.class).hasMessageContaining("Endereço não cadastrado para o cliente.");
	}

	@Test
	@DisplayName("Atualizar pessoa com sucesso.")
	void atualizar_AtualizarPessoa_ComSucesso() {
		// cenário
		Long idP1 = p1.getPessoa().getId();
		AtualizarPessoa dadosParaAtualizar = p2.getDadosParaAtualizar();
		when(pessoaRepository.findById(idP1)).thenReturn(Optional.of(p1.getPessoa()));
		
		// ação
		DetalhaPessoa dadosAtualizados = pessoaService.atualizar(idP1, dadosParaAtualizar);

		// verificação
		assertThat(dadosAtualizados.nome()).isEqualTo(p2.getDetalhaPessoa().nome());
		
		assertThat(dadosAtualizados.dataDeNascimento()).isEqualTo(p2.getDetalhaPessoa().dataDeNascimento());

	}

	@Test
	@DisplayName("Consultar pessoa por ID.")
	void consultarById_ConsultarPessoaPorID_ComSucesso() {
		// cenário
		Long idP1 = p1.getPessoa().getId();
		when(pessoaRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(p1.getPessoa()));

		// ação
		DetalhaPessoa infoPessoa = pessoaService.consultarById(idP1);

		// verificação
		assertThat(infoPessoa).isEqualTo(p1.getDetalhaPessoa());
	}

	@Test
	@DisplayName("Listar todos os clientes.")
	void listarClientes_ListaCliente_ComSucesso() {
		// cenário
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(p1.getPessoa());
		lista.add(p2.getPessoa());
		when(pessoaRepository.findAll()).thenReturn(lista);

		// ação
		List<DetalhaPessoa> listaClientes = pessoaService.getListaClientes();

		// verificação
		assertThat(listaClientes).isEqualTo(lista.stream().map(DetalhaPessoa::new).toList());
	}
	
	@Test
	@DisplayName("Listar todos quando não tem cliente.")
	void istarClientes_ListaCliente_QuandoNaoTemCliente() {
		// cenário
		List<Pessoa> lista = new ArrayList<Pessoa>();
		when(pessoaRepository.findAll()).thenReturn(lista);
		
		//ação
        Throwable e = Assertions.catchThrowable(() -> pessoaService.getListaClientes());

        //verificação
        assertThat(e).isInstanceOf(NoSuchElementException.class);
	}
	
	@Test
	@DisplayName("Paginar todos os clientes.")
	void getPaginaClientes_PaginarCliente_ComSucesso() {
		// cenário
		List<Pessoa> lista = new ArrayList<Pessoa>();
		lista.add(p1.getPessoa());
		lista.add(p2.getPessoa());
		Pageable pageable = PageRequest.of(1, 1);
		Page<Pessoa> pagePessoa = new PageImpl<Pessoa>(lista);
		when(pessoaRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(pagePessoa);
		
		// ação
		Page<DetalhaPessoa> listaClientes = pessoaService.getPaginaClientes(pageable);
		
		// verificação
		assertThat(listaClientes.getContent()).hasSize(2);
	}
	@Test
	@DisplayName("Paginar todos quando não tem cliente.")
	void getPaginaClientes_PaginarCliente_QuandoNaoTemCliente() {
		// cenário
		List<Pessoa> lista = new ArrayList<Pessoa>();
		Pageable pageable = PageRequest.of(1, 1);
		Page<Pessoa> pagePessoa = new PageImpl<Pessoa>(lista);
		when(pessoaRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(pagePessoa);
		
		// ação
		Throwable e = Assertions.catchThrowable(() -> pessoaService.getPaginaClientes(pageable));
		
		// verificação
		assertThat(e).isInstanceOf(NoSuchElementException.class);
	}
	
}
