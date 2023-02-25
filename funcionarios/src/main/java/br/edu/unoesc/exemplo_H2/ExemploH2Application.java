package br.edu.unoesc.exemplo_H2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;

import br.edu.unoesc.exemplo_H2.dto.FuncionarioDTO;
import br.edu.unoesc.exemplo_H2.model.Funcionario;
import br.edu.unoesc.exemplo_H2.service.FuncionarioService;

@SpringBootApplication
public class ExemploH2Application {
//	@Value("${mensagem}")
//	private String mensagem;
//	
//	@Value("${ambiente}")
//	private String ambiente;
	
	public static void main(String[] args) {
		SpringApplication.run(ExemploH2Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(FuncionarioService servico) {
		return args -> {
//			System.out.println(mensagem + " " + ambiente);
			
			servico.popularTabelaInicial();
			
			Funcionario f = new Funcionario(3L, "João", 0, new BigDecimal("2534"), LocalDate.of(1990, 8, 21));
			servico.incluir(f);
			
			FuncionarioDTO li = new FuncionarioDTO(f);
			System.out.println(li);
			
			// Exemplo de tratamento de exceções
			try {
				//System.out.println(10 / 0);
				servico.excluir(10L);			
			} catch (EmptyResultDataAccessException e) {
				System.out.println("\n>>> Erro! Registro não encontrado! <<<\n");
			} catch (RuntimeException e) {
				System.out.println("\n>>> Erro de execução! <<<\n" + e.getMessage());
			}
			
			// Exemplo de utilização da classe Optional
			Optional<Funcionario> fun = servico.porId(2L);
			if (fun.isEmpty()) {
				System.out.println("\n>>> Registro não encontrado! <<<\n");
			} else {
				System.out.println(fun);				
				System.out.println(fun.get());				
				System.out.println(fun.get().getNome());				
			}
			
			Funcionario a = servico.buscarPorId(5L);
			a.setNome("Hyoran");
			a.setNum_dep(10);
			a.setSalario(new BigDecimal("900"));
			
			if (a.getId() == null) {
				servico.incluir(a);
			} else {
				servico.alterar(a.getId(), a);
			}
			
			// Recupera todos os registros
			System.out.println(servico.listar());
			
			// Exemplos dos métodos de busca
			for (var funcionario: servico.buscarPorFaixaSalarial(new BigDecimal("100"), new BigDecimal("5000"))) {
				System.out.println(funcionario);
			}
			
			for (var funcionario: servico.buscarPossuiDependentes()) {
				System.out.println(funcionario);
			}
			
			for (var funcionario: servico.buscarPorNome("Nathan")) {
				System.out.println(funcionario);
			}
		};
	}
}

