package br.edu.unoesc.funcionarios.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.unoesc.funcionarios.DTO.FuncionarioDTO;
import br.edu.unoesc.funcionarios.model.Funcionario;
import br.edu.unoesc.funcionarios.service.FuncionarioService;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
//private Logger logger = Logger.getLogger(FuncionarioController.class.getName()); 
	
	@Autowired
	private FuncionarioService servico;
	
	@GetMapping
	public Iterable<Funcionario> listar() {
		return servico.listar();
	}
	
	@GetMapping("/listar-paginas")
	public ResponseEntity<Page<FuncionarioDTO>> listarPaginado(Pageable pagina) {
		return ResponseEntity.ok(servico.listarPaginado(pagina));
	}
	
	@RequestMapping(value = "/{id}",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<Funcionario> porId(@PathVariable Long id) {
			Optional<Funcionario> funcionario = servico.porId(id);

			if (funcionario.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok(funcionario.get());
		}
	
	@RequestMapping(value = "/xml/{id}", headers = "Accept=application/xml")
	public Funcionario porIdXML(@PathVariable Long id) {
		return servico.buscar(id);
	}
	
	@GetMapping("/find")
	List<Funcionario> porNome(@RequestParam("filtro") String nome) {
		return servico.buscarPorNome(nome);
	}
	
    @GetMapping("/faixa")
    public List<Funcionario> porFaixaSalarial(@RequestParam(value = "minimo", defaultValue = "0") Double minimo,
    						 				  @RequestParam(value = "maximo", defaultValue = "9999999999") Double maximo) {
    	return servico.buscarPorFaixaSalarial(new BigDecimal (minimo),new BigDecimal (maximo));
    }
    
    @PostMapping()
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> incluir(@RequestBody Funcionario funcionario) {
        funcionario = servico.incluir(funcionario);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        									 .path("/{id}")
        									 .buildAndExpand(funcionario.getId())
        									 .toUri();
        
        System.out.println(uri.toString());
        
        return ResponseEntity.created(uri).build();
    }
	
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable("id") Long id, @RequestBody Funcionario funcionario) {
    	if (servico.porId(id).isEmpty()) {
    		return ResponseEntity.notFound().build();
    	}
    	
        return ResponseEntity.ok(servico.alterar(id, funcionario));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    	try {
    		servico.excluir(id);   		
    	} catch (ObjectNotFoundException e) {
    		return ResponseEntity.notFound().build();
    	}
    	
    	return ResponseEntity.noContent().build();
    }
}
