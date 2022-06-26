package ifc.sisdi.coordinator.controller;

import ifc.sisdi.coordinator.model.Account;
import ifc.sisdi.coordinator.model.Action;
import ifc.sisdi.coordinator.model.Replica;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CoordinatorController {
	private AtomicInteger counter = new AtomicInteger();
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Action> logs  = new ArrayList<Action>();
	private ArrayList<Replica> replicas = new ArrayList<Replica>();

	public CoordinatorController() {
		this.accounts.add(new Account(1234, 100.00));
		this.accounts.add(new Account(4345, 50.00));
		this.accounts.add(new Account(5678, 250.00));

		this.replicas.add(new Replica("replica1", "http://localhost:8081"));
		this.replicas.add(new Replica("replica2", "http://localhost:8082"));
	}

	@GetMapping("/accounts")
	public ArrayList<Account> getAccounts(){
		return this.accounts;
	}

	@GetMapping("/replicas")
	public ArrayList<Replica> getReplicas(){
		return this.replicas;
	}
}
	
