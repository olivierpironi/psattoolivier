package br.com.attornatus.olivierpironi.domain.exception;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorExceptions {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> tratarNoSuchElement(NoSuchElementException e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> tratarValidacao(MethodArgumentNotValidException e) {
		var erros = e.getFieldErrors();
		return ResponseEntity.badRequest().body(erros.stream().map(DetalhamentoErros::new).toList());
	}

	@ExceptionHandler(PessoaNaoEncontradaException.class)
	public ResponseEntity<Object> tratarPessoaNaoEncontradaException(PessoaNaoEncontradaException e) {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(EnderecoNaoCadastrado.class)
	public ResponseEntity<Object> enderecoNaoCadastrado(EnderecoNaoCadastrado e) {
		return ResponseEntity.badRequest().body(new DetalhamentoException(e));
	}
	
	@ExceptionHandler(EntidadeExisteException.class)
	public ResponseEntity<Object> tratarEntidadeExisteException(EntidadeExisteException e) {
		return ResponseEntity.badRequest().body(new DetalhamentoException(e));
	}
	

	private record DetalhamentoErros(String field, String msg) {
		public DetalhamentoErros(FieldError erro) {
			this(erro.getField(), erro.getDefaultMessage());
		}

	}

}
