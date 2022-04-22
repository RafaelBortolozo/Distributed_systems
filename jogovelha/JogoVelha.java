package jogovelha;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class JogoVelha extends UnicastRemoteObject implements JogoVelhaInterface {

    private List<Jogador> jogadores;
    private int quantidadeJogadores;
    private int quantidadeJogadoresEmJogo;
    private int[][] matriz;

    protected JogoVelha() throws RemoteException {
        super();
        this.jogadores = new ArrayList<>();
        this.quantidadeJogadores = 0;
        this.quantidadeJogadoresEmJogo = 0;
        this.matriz = new int[3][3];
    }

    public void estouPronto(){
        quantidadeJogadoresEmJogo++;
    }

    public void sair(){
        quantidadeJogadoresEmJogo--;
    }

    public int getQuantidadeJogadoresEmJogo() {
        return quantidadeJogadoresEmJogo;
    }

    //verifica se o jogo pode ser iniciado, os dois jogadores devem estar online
    public boolean permissaoIniciarJogo(){
        if(this.quantidadeJogadoresEmJogo < 2){
            return false;
        } else{
            return true;
        }
    }

    //validar a jogada (jogada possivel apenas nas casas vazias)
    public boolean validarJogada(int linha, int coluna){
        linha--;
        coluna--;
        if(matriz[linha][coluna] == 0){
            return true;
        } else{
            return false;
        }
    }

    //retorna a permissao do jogador com base no seu id
    public boolean getPermissaoJogadorById(int id){
        for(Jogador jogador : jogadores) {
            if (jogador.getId() == id){
                return jogador.getPermissaoJogar();
            }
        }
        return false;
    }

    //verifica se o id ja foi usado ou eh invalido
    private boolean verificarJogador(Jogador jogador){
        if(jogador.getId() < 1) return true;

        for(Jogador j : jogadores){
            if((j.getId() == jogador.getId())) return true;
        }

        return false;
    }

    //Verificar se o jogador pode entrar e verificar seu id
    public MensagemRetorno entrar(Jogador jogador) throws RemoteException {
        MensagemRetorno mensagem = new MensagemRetorno();
        if(this.quantidadeJogadores < 2){
            if(!verificarJogador(jogador)){
                //primeiro jogador -> primeiro a jogar
                if(jogadores.isEmpty()){
                    jogador.setPermissaoJogar(true);
                }
                this.jogadores.add(jogador);
                this.quantidadeJogadores++;

                mensagem.setCod(1);
                mensagem.setTexto(jogador.getNome() + ", seja bem-vindo ao Jogo da Velha. Voce eh o jogador numero " + jogador.getId());

                return mensagem;
            }else{
                mensagem.setCod(2);
                mensagem.setTexto("Esse id eh invalido. Escolha outro.");
                return mensagem;
            }

        } else {
            mensagem.setCod(3);
            mensagem.setTexto("Não temos vagas. Volte mais tarde");
            return mensagem;
        }
    }

    //inverte as permissoes, permitindo a jogada do outro jogador
    public void alternarPermissoes() throws RemoteException{
        for(int i=0 ;i<2 ;i++){
            if(jogadores.get(i).getPermissaoJogar() == false){
                jogadores.get(i).setPermissaoJogar(true);
            }else{
                jogadores.get(i).setPermissaoJogar(false);
            }
        }
    }

    //funcao que realiza a jogada
    public void jogar(Jogador jogador, int linha, int coluna) throws RemoteException {
        linha--;
        coluna--;
        this.matriz[linha][coluna] = jogador.getId();
    }

    //verificar se deu velha
    public boolean verificarVelha(){
        for(int l=0 ;l<3 ;l++){
            for(int c=0 ;c<3 ;c++){
                if(matriz[l][c] == 0) return false;
            }
        }

        return true;
    }

    //verificar se alguem ganhou o jogo (OBS: desconsiderar o zero na analise)
    public boolean verificarVitoria(){
        int valor1;
        int valor2;
        int valor3;

        //verificar linhas
        for(int l=0 ; l<3 ;l++){
            valor1 = matriz[l][0];
            valor2 = matriz[l][1];
            valor3 = matriz[l][2];

            if((valor1 == valor2) && (valor2 == valor3)){
                if(valor1 != 0 || valor2 != 0 || valor3 != 0){
                    return true;
                }
            }
        }

        //verificar colunas
        for(int c=0 ; c<3 ;c++){
            valor1 = matriz[0][c];
            valor2 = matriz[1][c];
            valor3 = matriz[2][c];
            if((valor1 == valor2) && (valor2 == valor3)){
                if(valor1 != 0 || valor2 != 0 || valor3 != 0){
                    return true;
                }
            }
        }

        //verificar diagonal principal
        valor1 = matriz[0][0];
        valor2 = matriz[1][1];
        valor3 = matriz[2][2];
        if((valor1 == valor2) && (valor2 == valor3)){
            if(valor1 != 0 || valor2 != 0 || valor3 != 0){
                return true;
            }
        }

        //verificar diagonal secundaria
        valor1 = matriz[0][2];
        valor2 = matriz[1][1];
        valor3 = matriz[2][0];
        if((valor1 == valor2) && (valor2 == valor3)){
            if(valor1 != 0 || valor2 != 0 || valor3 != 0){
                return true;
            }
        }

        return false;
    }

    //retorna a String da matriz
    public String printMatriz() throws RemoteException {
        StringBuilder str = new StringBuilder();
        for(int c=0 ;c<3 ;c++){
            for(int l=0 ;l<3 ;l++){
                String valor = Integer.toString(matriz[c][l]);
                if(valor == "-1"){
                    valor = " ";
                }
                str.append("|" + valor);
                if(l == 2){
                    str.append("|\n");
                }
            }
        }
        return str.toString();
    }
}
