package ifc.sisdi.replica1.controller;

import ifc.sisdi.replica1.exception.FailException;
import ifc.sisdi.replica1.model.Account;
import ifc.sisdi.replica1.model.Action;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/accounts")
public class Replica1Controller {
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Action> logs  = new ArrayList<Action>();

	public Replica1Controller() {
		this.accounts.add(new Account(1234, 100.00));
		this.accounts.add(new Account(4345, 50.00));
		this.accounts.add(new Account(5678, 250.00));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public int sendAction(Action action) {
		this.logs.add(action);

		// 70% de chance de sucesso
		Random r = new Random();
		int error = r.nextInt(10) + 1; // 1-10
		System.out.println("ALEATORIO: " + error);
		if (error > 7) {
			throw new FailException();
		}

		return error;
	}

	@PutMapping
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void sendDecision(Map<String, String> decision){

		// Elimina a operacao bancaria
		if(decision.get("command").equals("abort")){
			for (Action action : this.logs){
				if (action.getId().equals(decision.get("id"))) this.logs.remove(action);
				return;
			}
		}

		// Executa a operacao bancaria
		for (Action action : this.logs){
			if (decision.get("id").equals(action.getId())) {
				for (Account account : this.accounts) {
					if (account.getNumberAccount() == action.getAccount()) {
						switch (action.getOperation()) {
							case "debit":
								account.setBalance(account.getBalance() - action.getValue());
								break;
							case "credit":
								account.setBalance(account.getBalance() + action.getValue());
								break;
						}
					}
				}
			}
		}
	}


	// Retorna voto "NO" em caso de erro
	@ControllerAdvice
	class Fail {
		@ResponseBody
		@ExceptionHandler(FailException.class)
		@ResponseStatus(HttpStatus.FORBIDDEN)
		String e(FailException e) {
			return e.getMessage();
		}
	}
}
	
