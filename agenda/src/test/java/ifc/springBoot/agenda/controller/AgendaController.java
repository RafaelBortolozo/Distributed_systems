package ifc.springBoot.agenda.controller;

import ifc.springBoot.agenda.exception.PessoaNaoEncontradaException;
import ifc.springBoot.agenda.model.Pessoa;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/pessoas")
public class AgendaController {
    private List<Pessoa> agenda = new ArrayList<>();
    private AtomicInteger contador = new AtomicInteger();

    @GetMapping
    public List<Pessoa> obterPessoas(){
        return this.agenda;
    }

    @GetMapping("/{id}")
    public Pessoa obterPessoa(@PathVariable Integer id){
        for (Pessoa pessoa: agenda) {
            if (pessoa.getId() == id)
                return pessoa;
        }

        throw new PessoaNaoEncontradaException(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa adicionarPessoa(@RequestBody Pessoa p){
        p.setId(this.contador.incrementAndGet());
        this.agenda.add(p);

        return p;
    }

    @PutMapping("/{id}")
    public Pessoa atualizarPessoa(@RequestBody Pessoa p, @PathVariable Integer id){
        for (Pessoa pessoa : agenda) {
            if (pessoa.getId() == id){
                pessoa.setEmail(p.getEmail());
                pessoa.setNome(p.getNome());

                return pessoa;
            }
        }

        throw new PessoaNaoEncontradaException(id);
    }

    @DeleteMapping("/{id}")
    public int excluirPessoa(@PathVariable Integer id){
        for (Pessoa pessoa : agenda) {
            if (pessoa.getId() == id){
                agenda.remove(pessoa);
                return id;
            }
        }

        throw new PessoaNaoEncontradaException(id);
    }

    @ControllerAdvice
    class PessoaNaoEncontrada {
        @ResponseBody
        @ExceptionHandler(PessoaNaoEncontradaException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String pessoaNaoEncontrada(PessoaNaoEncontradaException p){
            return p.getMessage();
        }
    }
}
