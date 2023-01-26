package br.com.attornatus.olivierpironi.domain.exception;

public record DetalhamentoException(String msg) {
	public DetalhamentoException(ExceptionPesonalizadas e) {
		this(e.getMessage());
	}
}