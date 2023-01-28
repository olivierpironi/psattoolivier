package br.com.attornatus.olivierpironi.domain.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.Endereco;
import br.com.attornatus.olivierpironi.domain.exception.EnderecoNaoCadastradoException;
import br.com.attornatus.olivierpironi.domain.exception.EntidadeExisteException;
import br.com.attornatus.olivierpironi.domain.exception.PessoaNaoEncontradaException;
import br.com.attornatus.olivierpironi.domain.pessoa.AtualizarPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.CadastroPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.DetalhaPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.Pessoa;
import br.com.attornatus.olivierpironi.infra.repository.PessoaRepository;

@Service
public class PessoaService {

	private static final PessoaNaoEncontradaException PESSOA_NAO_ENCONTRADA_EXCEPTION = new PessoaNaoEncontradaException("Pessoa com o ID especificado não encontrada");

	@Autowired
	private PessoaRepository pessoaRepository;

	private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public Pessoa cadastrar(CadastroPessoa dados) {
		Pessoa pessoa = new Pessoa(dados);
		pessoaRepository.save(pessoa);
		return pessoa;
	}

	public DetalhaPessoa consultarById(Long id) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> PESSOA_NAO_ENCONTRADA_EXCEPTION);
		return new DetalhaPessoa(pessoa);
	}
	
	public DetalhaPessoa atualizar(Long id, AtualizarPessoa dados) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> PESSOA_NAO_ENCONTRADA_EXCEPTION);
		dados.nome().ifPresent(pessoa::setNome);
		dados.dataNascimento().ifPresent(d -> pessoa.setDataNascimento(LocalDate.parse(d, formato)));
		return new DetalhaPessoa(pessoa);
	}
	
	public DetalhaPessoa atualizarEnderecoPrincipal(Long id, @Valid CadastroEndereco dados) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> PESSOA_NAO_ENCONTRADA_EXCEPTION);
		Endereco endereco = pessoa.getListaDeEnderecos()
        .stream()
        .filter(e -> e.equals(new Endereco(dados)))
        .findFirst()
        .orElseThrow(() -> new EnderecoNaoCadastradoException("Endereço não cadastrado para o cliente."));
			pessoa.setEnderecoPrincipal(endereco);
			return new DetalhaPessoa(pessoa);
	}

	public List<DetalhaEndereco> cadastrarEndereco(Long id, @Valid CadastroEndereco dados) {
		Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(NoSuchElementException::new);
		List<Endereco> lista = pessoa.getListaDeEnderecos();
		Endereco novoEndereco = new Endereco(dados);
		if (lista.contains(novoEndereco)) {throw new EntidadeExisteException("Endereço já cadastrado para este cliente.");}
		lista.add(novoEndereco);
		return lista.stream().map(DetalhaEndereco::new).toList();
		}
			
	
	public List<DetalhaPessoa> getListaClientes() {
		List<DetalhaPessoa> list = pessoaRepository.findAll().stream().map(DetalhaPessoa::new).toList();
		if(list.isEmpty()) {throw new NoSuchElementException();}
		return list;
	}
	
	public Page<DetalhaPessoa> getPaginaClientes(Pageable pagina) {
		Page<DetalhaPessoa> list = pessoaRepository.findAll(pagina).map(DetalhaPessoa::new);
		if(list.isEmpty()) {throw new NoSuchElementException();}
		return list;
	}

}
