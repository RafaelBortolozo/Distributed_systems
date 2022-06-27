package ifc.sisdi.coordinator.controller;

import ifc.sisdi.coordinator.exception.AccountNotFoundException;
import ifc.sisdi.coordinator.exception.FailException;
import ifc.sisdi.coordinator.model.Account;
import ifc.sisdi.coordinator.model.Action;
import ifc.sisdi.coordinator.model.Replica;
import org.apache.catalina.startup.FailedContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CoordinatorController {
	private AtomicInteger counter = new AtomicInteger();
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Action> logs  = new ArrayList<Action>();
	private ArrayList<Replica> replicas = new ArrayList<Replica>();
	HttpClient client = HttpClient.newHttpClient();

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

	private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
		var builder = new StringBuilder();
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			if (builder.length() > 0) {
				builder.append("&");
			}
			builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
		}
		System.out.println(builder.toString());
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}

	@ControllerAdvice
	class Fail {
		@ResponseBody
		@ExceptionHandler(FailException.class)
		@ResponseStatus(HttpStatus.FORBIDDEN)
		String fail(FailException e) {
			return e.getMessage();
		}
	}

	@ControllerAdvice
	class AccountNotFound {
		@ResponseBody
		@ExceptionHandler(AccountNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String fail(AccountNotFoundException e) {
			return e.getMessage();
		}
	}
}
	
