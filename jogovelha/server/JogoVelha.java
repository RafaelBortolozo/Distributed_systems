package jogovelha.server;

import jogovelha.cliente.Jogador;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class JogoVelha extends UnicastRemoteObject implements JogoVelhaInterface {

    private List<Jogador> jogadores;
    private int quantidadeJogadores;
    private int[][] matriz;

    protected JogoVelha() throws RemoteException {
        super();
        this.jogadores = new ArrayList<>();
        this.quantidadeJogadores = 0;
        this.matriz = new int[3][3];
        for(int l=0 ; l<3 ; l++){
            for(int c=0 ; c<3 ; c++){
                this.matriz[l][c] = -1;
            }
        }
    }

    public boolean validarJogada(int linha, int coluna){
        if(matriz[linha][coluna] != -1) return true;

        return false;
    }

    public boolean getPermissaoJogadorById(int id){
        for(Jogador jogador : jogadores) {
            if (jogador.getId() == id){
                return jogador.getPermissaoJogar();
            }
        }
        return false;
    }

    public void iniciarJogo(){
        //primeiro jogador cadastrado, primeiro a jogar
        jogadores.get(0).setPermissaoJogar(true);
    }

    private boolean verificarJogador(Jogador jogador){
        for(Jogador j : jogadores){
            if(j.getId() == jogador.getId()) return true;
        }

        return false;
    }

    public int getQuantidadeJogadores() {
        return quantidadeJogadores;
    }

    public MensagemRetorno entrar(Jogador jogador) throws RemoteException {
        MensagemRetorno mensagem = new MensagemRetorno();
        if(this.quantidadeJogadores < 2){
            if(!verificarJogador(jogador)){
                this.jogadores.add(jogador);
                this.quantidadeJogadores++;

                mensagem.setCod(1);
                mensagem.setTexto(jogador.getNome() + ", seja bem-vindo ao Jogo da Velha. Voce eh o jogador numero " + jogador.getId());

                return mensagem;
            }else{
                mensagem.setCod(2);
                mensagem.setTexto("Esse id já existe. Escolha outro.");
                return mensagem;
            }

        } else {
            mensagem.setCod(3);
            mensagem.setTexto("Não temos vagas. Volte mais tarde");
            return mensagem;
        }
    }

    public void alternarPermissoes() throws RemoteException{
        for(int i=0 ;i<2 ;i++){
            if(jogadores.get(i).getPermissaoJogar() == false){
                jogadores.get(i).setPermissaoJogar(true);
            }else{
                jogadores.get(i).setPermissaoJogar(false);
            }
        }
    }

    public void jogar(Jogador jogador, int linha, int coluna) throws RemoteException {
        this.matriz[linha][coluna] = jogador.getId();
    }

    public boolean verificarVitoria(){
        //verifica linhas
        for(int l=0 ; l<3 ;l++){
            if((this.matriz[l][0] == this.matriz[l][1]) && (this.matriz[l][1] == this.matriz[l][2])){
                return true;
            }
        }

        //verifica colunas
        for(int c=0 ; c<3 ;c++){
            if((this.matriz[0][c] == this.matriz[1][c]) && (this.matriz[1][c] == this.matriz[2][c])){
                return true;
            }
        }

        //verifica diagonal principal
        if((this.matriz[0][0] == this.matriz[1][1]) && (this.matriz[1][1] == this.matriz[2][2])){
            return true;
        }

        //verifica diagonal secundaria
        if((this.matriz[0][2] == this.matriz[1][1]) && (this.matriz[1][1] == this.matriz[3][0])){
            return true;
        }

        return false;
    }

    public String printMatriz() throws RemoteException {
        StringBuilder str = new StringBuilder();
        for(int c=0 ;c<3 ;c++){
            for(int l=0 ;l<3 ;l++){
                String valor = Integer.toString(matriz[l][c]);
                if(valor == "-1"){
                    valor = " ";
                }
                str.append("|" + valor);
                if(c == 2){
                    str.append("|\n");
                }
            }
        }
        return str.toString();
    }
}
