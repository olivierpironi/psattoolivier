package br.com.attornatus.olivierpironi.infra.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import br.com.attornatus.olivierpironi.domain.pessoa.AtualizarPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.CadastroPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.DetalhaPessoa;
import br.com.attornatus.olivierpironi.domain.service.PessoaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

	@Autowired
	PessoaService pessoaService;

	@PostMapping("/cadastro")
	@Transactional
	public ResponseEntity<DetalhaPessoa> criarPessoa(@RequestBody @Valid CadastroPessoa dados) { //TODO RETORNAR FIELDS DO ENDEREÇO QUE NÃO FORAM VALIDADOS
		var pessoa = pessoaService.cadastrar(dados);
		return ResponseEntity.created(URI.create(pessoa.getId().toString())).body(new DetalhaPessoa(pessoa));
	}
	
	@PatchMapping("/atualizar/{id}")
	@Transactional
	public ResponseEntity<DetalhaPessoa> atualizarPessoa(@PathVariable Long id, @RequestBody @Valid AtualizarPessoa dados) {
		return ResponseEntity.ok(pessoaService.atualizar(id, dados));
	}
	
	@PostMapping("/cadastrar-endereco/{id}")
	@Transactional
	public ResponseEntity<List<DetalhaEndereco>> cadastrarEndereco(@PathVariable Long id, @RequestBody @Valid CadastroEndereco dados) {
		return ResponseEntity.created(URI.create(id.toString())).body(pessoaService.cadastrarEndereco(id, dados));
	}
	
	@PatchMapping("/atualizar-endereco-principal/{id}")
	@Transactional
	public ResponseEntity<DetalhaPessoa> atualizarEnderecoPrincipal(@PathVariable Long id, @RequestBody @Valid CadastroEndereco dados) {
		return ResponseEntity.ok(pessoaService.atualizarEnderecoPrincipal(id, dados));
	}
	
	@GetMapping("/consultar/{id}")
	public ResponseEntity<DetalhaPessoa> consultarById(@PathVariable Long id) {
		return ResponseEntity.ok(pessoaService.consultarById(id));
	}
	
	@GetMapping("/listar-clientes")
	public ResponseEntity<List<DetalhaPessoa>> listarClientes() {
		return ResponseEntity.ok(pessoaService.getListaClientes());
	}
}
