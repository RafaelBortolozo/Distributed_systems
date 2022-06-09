package ifc.sisdi.agenda.exception;

public class PessoaNaoEncontradaException extends RuntimeException{
	
	public PessoaNaoEncontradaException(int id) {
		super("Não foi possı́vel encontrar pessoa com o id: " + id);
	}
}