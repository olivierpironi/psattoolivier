package br.com.attornatus.olivierpironi.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.attornatus.olivierpironi.domain.endereco.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

}
