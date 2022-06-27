package ifc.sisdi.coordinator.exception;

@SuppressWarnings("serial")
public class AccountNotFoundException extends RuntimeException{
	public AccountNotFoundException() {
		super("Não foi possı́vel encontrar conta");
	}
}