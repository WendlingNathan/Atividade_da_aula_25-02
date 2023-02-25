package br.edu.unoesc.exemplo_H2.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.unoesc.exemplo_H2.model.Funcionario;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class FuncionarioDTO implements Serializable {
//	Long id;
	String nome;
	Integer num_dep;
	BigDecimal salario;
	LocalDate nascimento;
	
	public FuncionarioDTO(Funcionario funcionario) {
//		this.id = funcionario.getId();
		this.nome = funcionario.getNome();
		this.num_dep = funcionario.getNum_dep();
		this.salario = funcionario.getSalario();
		this.nascimento = funcionario.getNascimento();
	}
}
