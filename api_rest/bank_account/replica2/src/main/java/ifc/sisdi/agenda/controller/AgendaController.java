package ifc.sisdi.agenda.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import ifc.sisdi.agenda.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ifc.sisdi.agenda.exception.PessoaNaoEncontradaException;
import ifc.sisdi.agenda.model.Pessoa;

@RestController
@RequestMapping("/pessoas")
public class AgendaController {
	
	//private List<Pessoa> agenda = new ArrayList<>();
	private AtomicInteger contador = new AtomicInteger();
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@GetMapping
	public List<Pessoa> obterTodasPessoas(){
		return this.pessoaRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Pessoa obterPessoa(@PathVariable int id) {
		Optional<Pessoa> pessoa = this.pessoaRepository.findById(id);

		if (pessoa.isEmpty())
			throw new PessoaNaoEncontradaException(id);

		return pessoa.get();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Pessoa adicionarPessoa(@RequestBody Pessoa pessoa) {
		return this.pessoaRepository.save(pessoa);
	}
	
	
	@PutMapping("/{id}")
	public Pessoa atualizarPessoa(@RequestBody Pessoa p, @PathVariable int id) {
		Optional<Pessoa> pessoa = this.pessoaRepository.findById(id);

		if(pessoa.isEmpty())
			throw new PessoaNaoEncontradaException(id);

		p.setId(id);
		return this.pessoaRepository.save(p);
	}
	
	@DeleteMapping("/{id}")
	public void excluirPessoa(@PathVariable int id) {
		//Optional<Pessoa> pessoa = this.pessoaRepository.findById(id);

		//if(pessoa.isEmpty())

		try {
			this.pessoaRepository.deleteById(id);
		}catch (Exception e){
			throw new PessoaNaoEncontradaException(id);
		}
	}
	
	@ControllerAdvice
	class PessoaNaoEncontra {
		@ResponseBody
		@ExceptionHandler(PessoaNaoEncontradaException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String pessoaNaoEncontrada(PessoaNaoEncontradaException p){
			return p.getMessage();
		}
	}
}