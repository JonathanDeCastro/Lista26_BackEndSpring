package br.edu.unoesc.funcionarios.service_impl;

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

import br.edu.unoesc.funcionarios.DTO.FuncionarioDTO;
import br.edu.unoesc.funcionarios.model.Funcionario;
import br.edu.unoesc.funcionarios.repository.FuncionarioRepository;
import br.edu.unoesc.funcionarios.service.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{
	@Autowired
	private FuncionarioRepository repositorio;
	
	@Override
	public void popularTabelaInicial() {
		repositorio.saveAll(List.of(
				new Funcionario(null, "João", 2, new BigDecimal("2000.00"), LocalDate.of(1998, 07, 9)),	
				new Funcionario(null, "Marcos", 4, new BigDecimal("3000.57"), LocalDate.of(1994, 10, 30)),	
				new Funcionario(null, "Maria", 1, new BigDecimal("2500"), LocalDate.of(1988, 11, 20)),	
				new Funcionario(null, "Marcelo", 0, new BigDecimal("1000"), LocalDate.of(1978, 01, 16)),	
				new Funcionario(null, "Ana", 2, new BigDecimal("7000"), LocalDate.of(1974, 03, 12)),	
				new Funcionario(null, "Amanda", 3, new BigDecimal("4000"), LocalDate.of(1991, 04, 17)),	
				new Funcionario(null, "Larissa", 1, new BigDecimal("2000"), LocalDate.of(1981, 9, 03)),	
				new Funcionario(null, "Bruna", 1, new BigDecimal("1700"), LocalDate.of(2002, 01, 14)),	
				new Funcionario(null, "Eduardo", 0, new BigDecimal("2800"), LocalDate.of(1994, 07, 21)),	
				new Funcionario(null, "Lucas", 0, new BigDecimal("2700"), LocalDate.of(1995, 12, 27)),	
				new Funcionario(null, "Natalia", 2, new BigDecimal("4200"), LocalDate.of(1996, 11, 02))	
			)
		);
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
						   () -> new ObjectNotFoundException("Funcionario não encontrado! Id: "
								   + id + ", Tipo: " + Funcionario.class.getName(), null)
				   );

		// Atualiza os dados do banco com os dados vindos da requisição
		f.setNome(funcionario.getNome());
		f.setNumDep(funcionario.getNumDep());
		f.setSalario(funcionario.getSalario());
		f.setNascimento(funcionario.getNascimento());

	return repositorio.save(f);
}

	@Override
	public void excluir(Long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
		} else {
			throw new ObjectNotFoundException("Funcionario não encontrado! Id: "
					   						  + id + ", Tipo: " + Funcionario.class.getName(), null);
		}
	}

	@Override
	public List<Funcionario> listar() {
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		
		// Recupera todos os registros da tabela
		Iterable<Funcionario> itens = repositorio.findAll();
		
		// Cria uma cópia dos dados na lista 'livros'
		itens.forEach(funcionarios::add);
		
		return funcionarios;
	}
	
	@Override
	public Page<FuncionarioDTO> listarPaginado(Pageable pagina) {
		Page<Funcionario> lista = repositorio.findAll(pagina);
		
		Page<FuncionarioDTO> listaDTO = lista.map(Funcionario -> new FuncionarioDTO(Funcionario));
		
		return listaDTO;
	}

	@Override
	public Funcionario buscar(Long id) {
		Optional<Funcionario> obj = repositorio.findById(id);
		
		return obj.orElseThrow(
						() -> new ObjectNotFoundException("Funcionario não encontrado! Id: "
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
		return repositorio.findByFiltro(nome);
	}

	@Override
	public List<Funcionario> buscarPorFaixaSalarial(BigDecimal salarioMinimo, BigDecimal salarioMaximo) {
		return repositorio.porSalario(salarioMinimo);
	}

	@Override
	public List<Funcionario> buscarPossuiDependentes(Integer numDependentes) {
		return repositorio.possuiDependentes();
	}

}
