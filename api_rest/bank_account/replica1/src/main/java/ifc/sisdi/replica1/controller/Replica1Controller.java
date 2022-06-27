package ifc.sisdi.replica1.controller;

import ifc.sisdi.replica1.exception.FailException;
import ifc.sisdi.replica1.model.Account;
import ifc.sisdi.replica1.model.Action;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
	public Action sendAction(Action action) {
		this.logs.add(action);


		// 70% de chance de sucesso
		Random r = new Random();
		int error = r.nextInt(10) + 1; // 1-10
		if (error > 7) {
			throw new FailException();
		}

		// Se a conta da requisicao bater com alguma conta da memoria
		// entao executa a operacao naquela conta (alteracao feita na replica)
		for (Account account : this.accounts) {
			if (account.getNumberAccount() == action.getAccount()) {
				switch (action.getOperation()) {
					case "debit":
						account.setBalance(account.getBalance() - action.getValue());
						System.out.println("New balance: " + account.getBalance());
						break;
					case "credit":
						account.setBalance(account.getBalance() + action.getValue());
						System.out.println("New balance: " + account.getBalance());
						break;
				}
			}
		}
		return action;
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
	
