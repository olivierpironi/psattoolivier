package br.com.attornatus.olivierpironi.domain.swagger;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.attornatus.olivierpironi.domain.exception.DetalhamentoException;
import br.com.attornatus.olivierpironi.domain.exception.EnderecoNaoCadastradoException;
import br.com.attornatus.olivierpironi.domain.exception.EntidadeExisteException;
import br.com.attornatus.olivierpironi.domain.exception.PessoaNaoEncontradaException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
public interface HandlerSwagger {

	@ApiOperation(value = "Tratar exceção de elemento não encontrado no banco de dados")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NOT FOUND") })
	public ResponseEntity<Object> tratarNoSuchElement(NoSuchElementException e);

	@ApiOperation(value = "Tratar exceção quando atributo é reprovado em uma validação.")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BAD REQUEST", response = List.class) })
	public ResponseEntity<Object> tratarValidacao(MethodArgumentNotValidException e);

	@ApiOperation(value = "Tratar exceção de quando busca-se uma pessoa que não existe no banco de dados.")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "NOT FOUND") })
	public ResponseEntity<Object> tratarPessoaNaoEncontradaException(PessoaNaoEncontradaException e);

	@ApiOperation(value = "Tratar exceção de quando tenta-se definir como endereço principal um endereço que não está cadastrado para um determinado cliente.")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BAD REQUEST", response = DetalhamentoException.class) })
	public ResponseEntity<Object> enderecoNaoCadastradoException(EnderecoNaoCadastradoException e);

	@ApiOperation(value = "Tratar exceção de quando cadastrar um endereço que já foi cadastrado para um cliente.")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "BAD REQUEST", response = DetalhamentoException.class) })
	public ResponseEntity<Object> tratarEntidadeExisteException(EntidadeExisteException e);

}
