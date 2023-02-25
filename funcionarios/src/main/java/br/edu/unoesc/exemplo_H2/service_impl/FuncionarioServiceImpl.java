package br.edu.unoesc.exemplo_H2.service_impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.unoesc.exemplo_H2.dto.FuncionarioDTO;
import br.edu.unoesc.exemplo_H2.model.Funcionario;
import br.edu.unoesc.exemplo_H2.repository.FuncionarioRepository;
import br.edu.unoesc.exemplo_H2.service.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
	@Autowired
	private FuncionarioRepository repositorio;
	
	@Override
	public void popularTabelaInicial() {
		repositorio.saveAll(List.of(
						new Funcionario(null, "João", 0, new BigDecimal("2522"), LocalDate.of(1999, 5, 17)),
						new Funcionario(null, "Thiago", 4, new BigDecimal("3531"), LocalDate.of(2015, 1, 13)),
						new Funcionario(null, "Mathias", 1, new BigDecimal("2584"), LocalDate.of(2012, 5, 26)),
						new Funcionario(null, "Joana", 2, new BigDecimal("7524"), LocalDate.of(2011, 2, 22)),
						new Funcionario(null, "Taina", 2, new BigDecimal("2134"), LocalDate.of(2000, 10, 10))
				)
		);
		
		Funcionario f = new Funcionario(null, "Carlos", 0, new BigDecimal("4000"), LocalDate.of(2010, 5, 20));
		f = repositorio.save(f);
	}

	@Override
	public Funcionario incluir(Funcionario funcionario) {
		funcionario.setId(null);
		return repositorio.save(funcionario);
	}

	@Override
	public Funcionario alterar(Long id, Funcionario funcionario) {
		var f = repositorio.findById(id)
						   .orElseThrow(
								   () -> new ObjectNotFoundException("Funcionário não encontrado! Id: "
										   + id + ", Tipo: " + Funcionario.class.getName(), null)
						   );
		
		// Atualiza os dados do banco com os dados vindos da requisição
		f.setNome(funcionario.getNome());
		f.setNum_dep(funcionario.getNum_dep());
		f.setSalario(funcionario.getSalario());
		f.setNascimento(funcionario.getNascimento());
		
		return repositorio.save(f);
	}

	@Override
	public void excluir(Long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
		} else {
			throw new ObjectNotFoundException("Funcionário não encontrado! Id: "
					   						  + id + ", Tipo: " + Funcionario.class.getName(), null);
		}
	}

	@Override
	public List<Funcionario> listar() {
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		
		// Recupera todos os registros da tabela
		Iterable<Funcionario> pessoas = repositorio.findAll();
		
		// Cria uma cópia dos dados na lista 'livros'
		pessoas.forEach(funcionarios::add);
		
		return funcionarios;
	}

	@Override
	public Funcionario buscar(Long id) {
		Optional<Funcionario> pessoa = repositorio.findById(id);
				
		return pessoa.orElseThrow(
						() -> new ObjectNotFoundException("Funcionário não encontrado! Id: "
					  		+ id + ", Tipo: " + Funcionario.class.getName(), null)
					);
	}

	@Override
	public Funcionario buscarPorId(Long id) {
		return repositorio.findById(id).orElse(new Funcionario());					      
	}

	@Override
	public Optional<Funcionario> porId(Long id) {
		return repositorio.findById(id);
	}

	@Override
	public List<Funcionario> buscarPorNome(String nome) {
		return repositorio.findByNomeContainingIgnoreCase(nome);
	}

	@Override
	public List<Funcionario> buscarPossuiDependentes() {
		return repositorio.possuiDependentes();
	}

	@Override
	public List<Funcionario> buscarPorFaixaSalarial(BigDecimal salarioMinimo, BigDecimal salarioMaximo) {
		return repositorio.porSalario(salarioMinimo, salarioMaximo);
	}

	@Override
	public Page<FuncionarioDTO> listarPaginado(Pageable pagina) {
		Page<Funcionario> lista = repositorio.findAll(pagina);
		
		Page<FuncionarioDTO> listaDTO = lista.map(livro -> new FuncionarioDTO(livro));
		
		return listaDTO;
	} 
}
