package ifc.springBoot.agenda.exception;

public class PessoaNaoEncontradaException extends RuntimeException {
    public PessoaNaoEncontradaException(Integer id){
        super("Não possível encontrar o contato com o id: " + id);
    }
}
