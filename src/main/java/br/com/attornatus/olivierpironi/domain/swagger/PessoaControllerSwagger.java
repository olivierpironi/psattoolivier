
package br.com.attornatus.olivierpironi.domain.swagger;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import br.com.attornatus.olivierpironi.domain.pessoa.AtualizarPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.CadastroPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.DetalhaPessoa;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
public interface PessoaControllerSwagger{

		@ApiOperation(httpMethod = "POST", value = "Endpoint para criação de pessoa", response = DetalhaPessoa.class)
	    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED")})
		public ResponseEntity<DetalhaPessoa> criarPessoa(@RequestBody @Valid CadastroPessoa dados);
		
		@ApiOperation(httpMethod = "PATCH", value = "Endpoint para atualizar informações de pessoa", response = DetalhaPessoa.class)
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
		public ResponseEntity<DetalhaPessoa> atualizarPessoa(@PathVariable Long id, @RequestBody @Valid AtualizarPessoa dados);
		
		@ApiOperation(httpMethod = "POST", value = "Endpoint para cadastrar endereços dentro de uma lista de endereços de uma pessoa", response = List.class)
	    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED")})
		public ResponseEntity<List<DetalhaEndereco>> cadastrarEndereco(@PathVariable Long id, @RequestBody @Valid CadastroEndereco dados);
		
		@ApiOperation(httpMethod = "PATCH", value = "Endpoint para endereço principal de uma pessoa", response = DetalhaPessoa.class)
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
		public ResponseEntity<DetalhaPessoa> atualizarEnderecoPrincipal(@PathVariable Long id, @RequestBody @Valid CadastroEndereco dados);
		
		@ApiOperation(httpMethod = "GET", value = "Consulta todas as informações de uma pessoa através do ID dela, incluindo a lista de endereços completa.", response = DetalhaPessoa.class)
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
		public ResponseEntity<DetalhaPessoa> consultarById(@PathVariable Long id);
		
		@ApiOperation(httpMethod = "GET", value = "Retorna a consulta de todas as pessoas cadastradas no banco de dados na forma de lista.", response = List.class)
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
		public ResponseEntity<List<DetalhaPessoa>> listarClientes();
		
		@ApiOperation(httpMethod = "GET", value = "Retorna a consulta de todas as pessoas cadastradas no banco de dados na forma de páginas.", response = Pageable.class)
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
		public ResponseEntity<Page<DetalhaPessoa>> paginasClientes(Pageable pagima);
	}



