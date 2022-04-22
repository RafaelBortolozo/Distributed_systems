package jogovelha;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class JogoCliente {
    public static void main(String[] args) {
        try{
            Registry registro = LocateRegistry.getRegistry(12345);
            JogoVelhaInterface jogo = (JogoVelhaInterface) registro.lookup("JogoVelha");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Forneca o nome do seu usuario: ");
            String nome = scanner.next();

            //o ID poderia ser gerado pelo proprio servidor
            System.out.print("Forneca o id do seu usuario: ");
            int id = scanner.nextInt();

            Jogador jogador = new Jogador(id, nome, false);

            MensagemRetorno msg = jogo.entrar(jogador);
            System.out.println("\n" + msg.getTexto());

            int opc = 0;
            int linha = -1;
            int coluna = -1;
            boolean jogadaValida;
            boolean breakLoopMessage = false;

            if(msg.getCod() == 1){
                do{
                    System.out.println("\nJogo da velha");
                    System.out.println("1 - Jogar");
                    System.out.println("0 - Sair");
                    System.out.print("Opcao: ");
                    opc = scanner.nextInt();
                    System.out.println();

                    if(opc == 1){
                        jogo.estouPronto();

                        //verificar se tem 2 jogadores online
                        while(!jogo.permissaoIniciarJogo()){
                            if(!breakLoopMessage){
                                System.out.println("Procurando oponente...");
                                breakLoopMessage = true;
                            }
                        }
                        System.out.println("Oponente encontrado!");
                        breakLoopMessage = false;

                        //enquanto alguem nao ganhar...
                        while(!jogo.verificarVitoria()){
                            //verifica se deu velha
                            if(jogo.verificarVelha()){
                                System.out.println("VELHA!");
                                jogo.sair();
                                return;
                            }

                            //verifica se o jogador tem permissão para jogar, se não, espere
                            if(jogo.getPermissaoJogadorById(id)){
                                //loop caso o jogador lance uma jogada invalida
                                do{
                                    System.out.println("\nEh a sua vez!");
                                    System.out.println(jogo.printMatriz());
                                    System.out.print("Digite a linha: ");
                                    linha = scanner.nextInt();
                                    System.out.print("Digite a coluna: ");
                                    coluna = scanner.nextInt();
                                    System.out.println("");

                                    //Se a jogada nao for valida, então jogue novamente
                                    //se for valida, a jogada é registrada e verifica se ganhou
                                    jogadaValida = jogo.validarJogada(linha, coluna);
                                    if(!jogadaValida){
                                        System.out.println("Jogada invalida, escolha outra posicao.\n");
                                    }else{
                                        jogo.jogar(jogador,linha,coluna);
                                        jogo.alternarPermissoes();
                                        if(jogo.verificarVitoria()){
                                            System.out.println(jogo.printMatriz());
                                            System.out.println("PARABENS, VOCE GANHOU!!!");
                                            jogo.sair();

                                            return;
                                        }
                                        breakLoopMessage = false;
                                    }
                                }while(!jogadaValida);

                            } else {
                                if(!breakLoopMessage){
                                    System.out.println("\n" + jogo.printMatriz());
                                    System.out.println("Aguardando jogada do oponente...");
                                    breakLoopMessage = true;
                                }
                            }
                        }
                        System.out.println(jogo.printMatriz());
                        System.out.println("Que pena, voce perdeu.");
                        jogo.sair();
                        return;
                    }
                }while(opc != 0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
