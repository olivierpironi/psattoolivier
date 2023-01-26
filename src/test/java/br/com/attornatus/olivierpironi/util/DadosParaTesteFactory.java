package br.com.attornatus.olivierpironi.util;

import java.util.ArrayList;
import java.util.Optional;

import br.com.attornatus.olivierpironi.domain.endereco.CadastroEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.DetalhaEndereco;
import br.com.attornatus.olivierpironi.domain.endereco.Endereco;
import br.com.attornatus.olivierpironi.domain.pessoa.AtualizarPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.CadastroPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.DetalhaPessoa;
import br.com.attornatus.olivierpironi.domain.pessoa.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DadosParaTesteFactory {
	
	public static Long contaNovaPessoa =0l;
	
	public Pessoa pessoa;
	public CadastroPessoa CadastroPessoa;
	public CadastroEndereco CadastroEndereco;
	public DetalhaPessoa DetalhaPessoa;
	public ArrayList<DetalhaEndereco> ArrayEnderecos = new ArrayList<>();
	public AtualizarPessoa dadosParaAtualizar; 
	
	

	public DadosParaTesteFactory criarPessoa1() {
		CadastroEndereco = new CadastroEndereco("Rua Jatobás", "32315110", "678", "Contagem");
		CadastroPessoa = new CadastroPessoa("Olivier", "01/03/1999", CadastroEndereco);
		pessoa = new Pessoa(CadastroPessoa);
		pessoa.setId(0l);
		DetalhaPessoa = new DetalhaPessoa(pessoa);
		ArrayEnderecos.add(new DetalhaEndereco(pessoa.getEnderecoPrincipal()));
		dadosParaAtualizar = new AtualizarPessoa(Optional.of( "Olivier"),Optional.of( "01/03/1999"));
		pessoa.getEnderecoPrincipal().setId(0);
		return this;
	}
	
	public DadosParaTesteFactory criarPessoa2() {
		CadastroEndereco = new CadastroEndereco("Rua Jaguatirica", "33315110", "378", "Belo Horizonte");
		CadastroPessoa = new CadastroPessoa("Joana", "03/03/1999", CadastroEndereco);
		pessoa = new Pessoa(CadastroPessoa);
		pessoa.setId(0l);
		DetalhaPessoa = new DetalhaPessoa(pessoa);
		ArrayEnderecos.add(new DetalhaEndereco(pessoa.getEnderecoPrincipal()));
		dadosParaAtualizar = new AtualizarPessoa(Optional.of( "Joana"),Optional.of( "03/03/1999"));
		pessoa.getEnderecoPrincipal().setId(0);
		return this;
	}
	public Pessoa criarPessoa() {
		contaNovaPessoa++;
		CadastroEndereco = new CadastroEndereco(contaNovaPessoa.toString(), "33315110", "378", "Belo Horizonte");
		CadastroPessoa = new CadastroPessoa("Joana", "03/03/1999", CadastroEndereco);
		return new Pessoa(CadastroPessoa);
		
		
	}
	
	public DadosParaTesteFactory criarPessoaCompleta() {
		contaNovaPessoa++;
		CadastroEndereco = new CadastroEndereco(contaNovaPessoa.toString(), "33315110", "378", "Belo Horizonte");
		CadastroPessoa = new CadastroPessoa("Joana", "03/03/1999", CadastroEndereco);
		pessoa = new Pessoa(CadastroPessoa);
		return this;
		
		
	}
	public void adicionarEndereco(CadastroEndereco e) {
		Endereco endereco = new Endereco(e);
		endereco.setId(Integer.valueOf(String.valueOf(contaNovaPessoa)));
		ArrayEnderecos.add(new DetalhaEndereco(endereco));
	}
	
}
