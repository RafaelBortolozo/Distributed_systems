package jogovelha.cliente;

import jogovelha.server.JogoVelha;
import jogovelha.server.JogoVelhaInterface;
import jogovelha.server.MensagemRetorno;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class JogoCliente {
    public static void main(String[] args) {
        try{
            Registry registro = LocateRegistry.getRegistry(12345);
            JogoVelhaInterface jogo = (JogoVelhaInterface) registro.lookup("JogoVelha");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Forneca o nome do seu usuário:");
            String nome = scanner.next();

            //o ID poderia ser gerado pelo proprio servidor
            System.out.println("Forneca o id do seu usuário:");
            int id = scanner.nextInt();

            Jogador jogador = new Jogador(id, nome);

            MensagemRetorno msg = jogo.entrar(jogador);
            System.out.println(msg.getTexto());
            int opc = 0;
            if(msg.getCod() == 1){
                do{
                    System.out.println("1 - Jogar");
                    System.out.println("0 - Sair");
                    System.out.println("Opcao: ");
                    opc = scanner.nextInt();

                    if(opc == 1){
                        System.out.println("Sua vez. Pode jogar!");
                    }
                }while(opc != 0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
