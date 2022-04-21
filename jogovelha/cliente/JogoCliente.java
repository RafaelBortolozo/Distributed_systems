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
            System.out.print("Forneca o nome do seu usuario:");
            String nome = scanner.next();

            //o ID poderia ser gerado pelo proprio servidor
            System.out.print("Forneca o id do seu usuario:");
            int id = scanner.nextInt();

            Jogador jogador = new Jogador(id, nome, false);

            MensagemRetorno msg = jogo.entrar(jogador);
            System.out.println(msg.getTexto());

            int opc = 0;
            int linha = -1;
            int coluna = -1;
            boolean jogadaValida;
            boolean repeatMessage = true;

            if(msg.getCod() == 1){
                do{
                    System.out.println("\nJogo da velha");
                    System.out.println("1 - Jogar");
                    System.out.println("0 - Sair");
                    System.out.println("Opcao: ");
                    opc = scanner.nextInt();

                    if(opc == 1){
                        //enquanto nao acabar o jogo...
                        while(jogo.verificarVitoria()){

                            //verifica se o jogador tem permissão para jogar, se não, espere
                            if(jogo.getPermissaoJogadorById(id)){
                                //loop caso o jogador lance uma jogada invalida
                                do{
                                    System.out.println("Eh a sua vez!");
                                    System.out.println(jogo.printMatriz());
                                    System.out.print("\nDigite a linha(1-3): ");
                                    linha = scanner.nextInt();
                                    System.out.print("\nDigite a coluna(1-3): ");
                                    coluna = scanner.nextInt();

                                    //Se a jogada nao for valida, então jogue novamente
                                    //se for valida, a jogada é registrada e verifica se ganhou
                                    jogadaValida = jogo.validarJogada(linha, coluna);
                                    if(!jogadaValida){
                                        System.out.println("Jogada invalida, escolha outra posicao.\n");
                                    }else{
                                        jogo.jogar(jogador,linha,coluna);
                                        jogo.alternarPermissoes();
                                        if(jogo.verificarVitoria()){
                                            System.out.println("PARABENS, VOCE GANHOU!!!");
                                            break;
                                        }
                                    }
                                }while(!jogadaValida);

                            } else {
                                if(repeatMessage){
                                    System.out.println("Aguardando jogada do adversario...");
                                    repeatMessage = false;
                                }
                            }
                        }
                        System.out.println("Que pena, voce perdeu.");
                    }
                }while(opc != 0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
