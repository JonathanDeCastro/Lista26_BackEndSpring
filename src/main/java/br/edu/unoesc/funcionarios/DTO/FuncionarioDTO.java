package br.edu.unoesc.funcionarios.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.unoesc.funcionarios.model.Funcionario;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class FuncionarioDTO implements Serializable{
	
	String nome;
	Integer numDep;
	BigDecimal salario;
	LocalDate nascimento;
	
	public FuncionarioDTO(Funcionario funcionario) {
		this.nome = funcionario.getNome();
		this.numDep = funcionario.getNumDep();
		this.salario = funcionario.getSalario();
		this.nascimento = funcionario.getNascimento();
	}

}
