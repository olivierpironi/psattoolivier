package br.com.attornatus.olivierpironi.domain.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.Endereco;
import br.com.attornatus.olivierpironi.domain.exception.EnderecoNaoCadastrado;
import br.com.attornatus.olivierpironi.domain.exception.EntidadeExisteException;
import br.com.attornatus.olivierpironi.domain.exception.PessoaNaoEncontradaException;
import br.com.attornatus.olivierpironi.domain.pessoa.AtualizarPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.CadastroPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.DetalhaPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.Pessoa;
import br.com.attornatus.olivierpironi.infra.repository.EnderecoRepository;
import br.com.attornatus.olivierpironi.infra.repository.PessoaRepository;
import jakarta.validation.Valid;
import net.jpountz.xxhash.XXHashFactory;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;

	private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public Pessoa cadastrar(CadastroPessoa dados) {
		Pessoa pessoa = new Pessoa(dados);
		pessoaRepository.save(pessoa);
		return pessoa;
	}

	public DetalhaPessoa consultarById(Long id) {
		return new DetalhaPessoa(pessoaRepository.findById(id).get());
	}
	
	public DetalhaPessoa atualizar(Long id, AtualizarPessoa dados) {
		Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa com o ID especificado não encontrada"));
		dados.nome().ifPresent(pessoa::setNome);
		dados.dataNascimento().ifPresent(d -> pessoa.setDataNascimento(LocalDate.parse(d, formato)));
		return new DetalhaPessoa(pessoa);
	}
	
	public DetalhaPessoa atualizarEnderecoPrincipal(Long id, @Valid CadastroEndereco dados) {
		Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa com o ID especificado não encontrada"));
		System.err.println(pessoa.getListaDeEnderecos());
		Optional<Endereco> endereco = pessoa.getListaDeEnderecos().stream().filter(e -> e.getId().equals(simularId(dados))).findFirst();
		if (endereco.isPresent()) {
			pessoa.setEnderecoPrincipal(endereco.get());
			return new DetalhaPessoa(pessoa);
		} else {
			throw new EnderecoNaoCadastrado("Endereço não cadastrado para o cliente.");
		}

	}

	public int simularId(CadastroEndereco dados) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		byte[] bytes = (dados.logradouro() + dados.cep() + dados.numero() + dados.cidade()).getBytes();
		int hash = factory.hash32().hash(bytes, 0, bytes.length, 0);
		return hash;
	}

	public List<DetalhaEndereco> cadastrarEndereco(Long id, @Valid CadastroEndereco dados) {
		Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
		if(pessoaOptional.isEmpty()) {throw new NoSuchElementException();}
		Pessoa pessoa = pessoaOptional.get();
		List<Endereco> lista = pessoa.getListaDeEnderecos();
		
		Optional<Endereco> opEndereco = enderecoRepository.findById((simularId(dados)));
//		if(!opEndereco.isEmpty()) {
//			lista.add(opEndereco.get());
//			return lista.stream().map(DetalhaEndereco::new).toList();
//		}
		Endereco e = new Endereco(dados);
		if (!lista.contains(e)) {
			lista.add(e);
			return lista.stream().map(DetalhaEndereco::new).toList();
		}
		throw new EntidadeExisteException("Endereço já cadastrado para este cliente.");
	}
	
	public List<DetalhaPessoa> getListaClientes() {
		List<DetalhaPessoa> list = pessoaRepository.findAll().stream().map(DetalhaPessoa::new).toList();
		if(list.isEmpty()) {
			throw new NoSuchElementException(); //TODO garantir teste disso
		}
		return list;
	}

}
