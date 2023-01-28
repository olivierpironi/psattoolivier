package br.com.attornatus.olivierpironi.domain.exception;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "DTO utilizada para retornar informações sobre ocorrência de alguma exception.")
public record DetalhamentoException(String msg) {
	public DetalhamentoException(ExceptionPesonalizadas e) {
		this(e.getMessage());
	}
}