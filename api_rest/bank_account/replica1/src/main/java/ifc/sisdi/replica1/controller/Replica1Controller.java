package ifc.sisdi.replica1.controller;

import ifc.sisdi.replica1.exception.FailException;
import ifc.sisdi.replica1.model.Account;
import ifc.sisdi.replica1.model.Action;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping("/accounts")
public class Replica1Controller {
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Action> actions  = new ArrayList<Action>();

	public Replica1Controller() {
		this.accounts.add(new Account(1234, 100.00));
		this.accounts.add(new Account(4345, 50.00));
		this.accounts.add(new Account(5678, 250.00));
	}

	@PostMapping
	public ResponseEntity<Object> sendAction(Action action) throws IOException, InterruptedException {
		this.actions.add(action);

		// 70% de chance de sucesso
		Random r = new Random();
		int error = r.nextInt(10) + 1; // 1-10
		if (error > 7) {
			return ResponseEntity.status(HttpStatus.OK).body(false);
		}

		return ResponseEntity.status(HttpStatus.OK).body(true);
	}

	// Executa a operacao bancaria
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public void commit(@PathVariable String id) throws IOException, InterruptedException {
		for (Action action : this.actions){
			if (id.equals(action.getId())) {
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

	// Elimina a operacao bancaria
	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void rollback(String id) throws IOException, InterruptedException {
		for (Action action : this.actions){
			if (action.getId().equals(id)) this.actions.remove(action);
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
	
