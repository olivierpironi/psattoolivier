package br.com.attornatus.olivierpironi.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.attornatus.olivierpironi.domain.pessoa.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
