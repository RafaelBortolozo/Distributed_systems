package ifc.sisdi.coordinator.exception;

public class AccountNotFoundException extends RuntimeException{
	
	public AccountNotFoundException(int id) {
		super("Não foi possı́vel encontrar pessoa com o id: " + id);
	}
}