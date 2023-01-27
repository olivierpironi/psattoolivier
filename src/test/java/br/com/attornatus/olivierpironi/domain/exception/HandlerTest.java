package br.com.attornatus.olivierpironi.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;


@WebMvcTest(Handler.class)
@DisplayName("Testes para o Handler")
class HandlerTest {

	@Autowired
	Handler handler;

	@Test
	@DisplayName("Testa NoSuchElementException")
	void tratarNoSuchElement() {
		// cenário
		NoSuchElementException exception = new NoSuchElementException();
		
		// ação
		ResponseEntity<Object> response = handler.tratarNoSuchElement(exception);
		
		// verificação
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		
		assertThat(response.getBody()).isNull();		
	}
	
	@Test
	@DisplayName("Testa PessoaNaoEncontradaException")
	void tratarPessoaNaoEncontradaException() {
		// cenário
		PessoaNaoEncontradaException exception = new PessoaNaoEncontradaException("");
		
		// ação
		ResponseEntity<Object> response = handler.tratarPessoaNaoEncontradaException(exception);
		
		// verificação
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		
		assertThat(response.getBody()).isNull();		
	}
	
	@Test
	@DisplayName("Testa EnderecoNaoCadastradoException")
	void EnderecoNaoCadastrado() {
		// cenário
		EnderecoNaoCadastradoException exception = new EnderecoNaoCadastradoException("Endereço não cadastrado.");
		
		// ação
		ResponseEntity<Object> response = handler.enderecoNaoCadastradoException(exception);
		
		// verificação
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		assertThat(response.getBody()).isEqualTo(new DetalhamentoException(exception));		
	}
	
	@Test
	@DisplayName("Testa EntidadeExisteException")
	void tratarEntidadeExisteException() {
		// cenário
		EntidadeExisteException exception = new EntidadeExisteException("Endereço não cadastrado.");
		
		// ação
		ResponseEntity<Object> response = handler.tratarEntidadeExisteException(exception);
		
		// verificação
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		assertThat(response.getBody()).isEqualTo(new DetalhamentoException(exception));	
	}
	
	@Test
	@DisplayName("Testa MethodArgumentNotValidException")
	void tratarValidacao() {
		// cenário
		BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "algo");
		MethodParameter parameter= null;
		MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);
		
		// ação
		ResponseEntity<Object> response = handler.tratarValidacao(exception);
		
		// verificação
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
	}

}
