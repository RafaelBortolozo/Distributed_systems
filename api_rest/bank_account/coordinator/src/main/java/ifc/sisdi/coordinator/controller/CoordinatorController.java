package ifc.sisdi.coordinator.controller;

import ifc.sisdi.coordinator.exception.AccountNotFoundException;
import ifc.sisdi.coordinator.exception.FailException;
import ifc.sisdi.coordinator.model.Account;
import ifc.sisdi.coordinator.model.Action;
import ifc.sisdi.coordinator.model.Replica;
import org.apache.catalina.startup.FailedContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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

	// Pega todas as contas
	@GetMapping("/accounts")
	public ArrayList<Account> getAccounts(){
		return this.accounts;
	}

	// Pega todas as replicas (creio que aqui era pra fazer requisicao de
	// endpoint para as replicas, não apenas adicionar uma string da URL no construtor)
	@GetMapping("/replicas")
	public ArrayList<Replica> getReplicas(){
		return this.replicas;
	}

	// Construcao, envio, validacao e execucao das acoes
	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public Action sendAction(@RequestBody Action action) throws IOException, InterruptedException {

		// Se existir uma conta com aquele numero...
		for (Account account : this.accounts) {
			if (account.getNumberAccount() == action.getAccount()) {

				// Cria identificador unico para a acao
				UUID uuid = UUID.randomUUID();
				String uuidString = uuid.toString();
				action.setId(uuidString);

				logs.add(action);

				// Montagem do objeto para criacao da requisicao
				Map<Object, Object> data = new HashMap<>();

				data.put("id", action.getId());
				data.put("operation", action.getOperation());
				data.put("account", action.getAccount());
				data.put("value", action.getValue());

				// Envio dos logs contendo a acao para as replicas
				// As replicas validam as acoes e retornam status de sucesso
				// ou retornam erro em caso de fracasso, quebrando a acao para todos os hosts
				for (Replica replica : this.replicas) {
					HttpRequest request = HttpRequest.newBuilder().POST(buildFormDataFromMap(data))
							.uri(URI.create(replica.getEndpoint())).build();
					HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
					if (response.statusCode() == 403) {
						throw new FailException();
					}
				}

				// Executa a acao no coordenador (falta impedir saldo negativo)
				switch (action.getOperation()) {
					case "debit":
						account.setBalance(account.getBalance() - action.getValue());
						break;
					case "credit":
						account.setBalance(account.getBalance() + action.getValue());
						break;
				}

				// por fim, remove o log (n sei se é pra remover ou manter na memória)
				logs.remove(action);

				return action;
			}
		}
		throw new AccountNotFoundException(action.getAccount());
	}

	// Construcao daquele trecho de parametros da URL de uma requisicao GET
	// ex: id=b641fd13-7573-4a98-bbb9-1d07f20ad68a&operation=debit&value=10.0&account=1234
	// Ela eh usada na sendAction() aos enviar requisições POST para as replicas.
	// A funcao abaixo retorna um negocio estranho, que pode ser visualizado no ultimo print
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
		System.out.println("URL_PARAMETROS: " + builder.toString());
		System.out.println("RESULTADO DUVIDOSO: " + HttpRequest.BodyPublishers.ofString(builder.toString()));
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}

	// Funcao de erro, executada caso uma das replicas retornam status 4**
	@ControllerAdvice
	class Fail {
		@ResponseBody
		@ExceptionHandler(FailException.class)
		@ResponseStatus(HttpStatus.FORBIDDEN)
		String fail(FailException e) {
			return e.getMessage();
		}
	}

	// Funcao de erro para conta nao encontrada
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
	
