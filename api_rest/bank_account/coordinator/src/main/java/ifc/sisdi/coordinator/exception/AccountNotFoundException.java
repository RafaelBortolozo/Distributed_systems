package ifc.sisdi.coordinator.exception;

@SuppressWarnings("serial")
public class AccountNotFoundException extends RuntimeException{
	public AccountNotFoundException(int id) {
		super("Not found account: " + id);
	}
}