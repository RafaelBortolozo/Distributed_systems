package ifc.sisdi.coordinator.controller;

import ifc.sisdi.coordinator.exception.AccountNotFoundException;
import ifc.sisdi.coordinator.exception.FailException;
import ifc.sisdi.coordinator.exception.MessageException;
import ifc.sisdi.coordinator.model.Account;
import ifc.sisdi.coordinator.model.Action;
import ifc.sisdi.coordinator.model.Decision;
import ifc.sisdi.coordinator.model.Replica;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CoordinatorController {
	private AtomicInteger counter = new AtomicInteger();
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Action> logs  = new ArrayList<Action>();
	private ArrayList<Replica> replicas = new ArrayList<Replica>();
	private ArrayList<Decision> decisions = new ArrayList<Decision>();
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

	// Etapa de envio de uma acao para as replicas e retornar os votos
	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public void sendAction(@RequestBody Action action) throws IOException, InterruptedException {
		ArrayList<Boolean> votes = new ArrayList<Boolean>();
		votes.clear();

		// Se existir uma conta com aquele numero...
		for (Account account : this.accounts) {
			if (account.getNumberAccount() == action.getAccount()) {

				// Cria identificador unico para a acao
				UUID uuid = UUID.randomUUID();
				String uuidString = uuid.toString();
				action.setId(uuidString);

				logs.add(action);

				// Montagem do objeto para envio da acao
				Map<Object, Object> data = new HashMap<>();

				data.put("id", action.getId());
				data.put("operation", action.getOperation());
				data.put("account", action.getAccount());
				data.put("value", action.getValue());

				// Envio do log contendo a acao para as replicas
				// As replicas registram a acao e retornam um status
				for (Replica replica : this.replicas) {
					HttpRequest request = HttpRequest.newBuilder().POST(buildFormDataFromMap(data))
							.uri(URI.create(replica.getEndpoint())).build();
					HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
					if (response.statusCode() == 403) {
						System.out.println("VOTEI NAO");
						// status: 403 - FORBIDDEN
						votes.add(false);
					}else{
						System.out.println("VOTEI SIM");
						// status: 201 - CREATED
						votes.add(true);
					}
				}

				// Verifica os votos e registra a decisao como FALSE ou TRUE
				int count = 0;
				System.out.println("TAMANHO: " + votes.size());
				for (boolean vote : votes) {
					if (vote){
						count++;
					}
				}
				if (count == votes.size()) {
					this.decisions.add(new Decision(action.getId(), true));
					System.out.print("NOVA ACAO VALIDA: " + action.getId());
				} else {
					this.decisions.add(new Decision(action.getId(), false));
					System.out.print("NOVA ACAO INVALIDA: " + action.getId());
				}
				return;
			}
		}
		// Conta nao encontrada
		throw new AccountNotFoundException(action.getAccount());
	}

	// Etapa de envio do id de uma acao para as replicas
	// Executar ou abortar a acao
	@PutMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public void sendDecision(@RequestBody String id) throws IOException, InterruptedException {
		Map<Object, Object> data = new HashMap<>();

		// Se existir uma acao com o mesmo id informado...
		for (Action action : this.logs) {
			if (action.getId().equals(id)) {

				// Se a decisao da acao for TRUE, entao executa a acao
				for (Decision decision : this.decisions){
					if (decision.isDecision()){

						// Executa a acao no coordenador
						for (Account account : accounts){
							if(account.getNumberAccount() == action.getAccount()){
								switch (action.getOperation()) {
									case "debit":
										account.setBalance(account.getBalance() - action.getValue());
										break;
									case "credit":
										account.setBalance(account.getBalance() + action.getValue());
										break;
								}

								// Monta o objeto que sera enviado para as replicas
								data.put("id", decision.getId());
								data.put("command", "commit");

								// Apague a decisao
								this.decisions.remove(decision);

							}
						}
					// Se a decisao da acao for FALSE, entao descarta a acao
					} else {
						// Monta o objeto que sera enviado para as replicas
						data.put("id", decision.getId());
						data.put("command", "abort");
					}
				}

				// Remove a acao do coordenador (Pra evitar repeticoes de transacao)
				// Envia o objeto para as replicas
				// Salvar transações em um arquivo se for necessário.
				this.logs.remove(action);
				for (Replica replica : this.replicas) {
					HttpRequest request = HttpRequest.newBuilder().PUT(buildFormDataFromMap(data))
							.uri(URI.create(replica.getEndpoint())).build();
					HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
					if (response.statusCode() == 404) {
						throw new FailException();
					}
				}

				return;
			}
		}
		// Se a acao nao existir, entao retorna um status 400 - BAD REQUEST
		throw new FailException();

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
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}


//	@ControllerAdvice
//	class MessageExcept {
//		@ResponseBody
//		@ExceptionHandler(MessageException.class)
//		@ResponseStatus(HttpStatus.FORBIDDEN)
//		String e(MessageException e) {
//			return e.getMessage();
//		}
//	}

// Funcao de erro, executada caso uma das replicas retornam status 4**
	@ControllerAdvice
	class Fail {
		@ResponseBody
		@ExceptionHandler(FailException.class)
		@ResponseStatus(HttpStatus.FORBIDDEN)
		String e(FailException e) {
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
	
