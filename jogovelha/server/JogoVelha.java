package jogovelha.server;

import jogovelha.cliente.Jogador;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class JogoVelha extends UnicastRemoteObject implements JogoVelhaInterface {

    private List<Jogador> jogadores;
    private int quantidadeJogadores;

    protected JogoVelha() throws RemoteException {
        super();
        this.jogadores = new ArrayList<>();
        this.quantidadeJogadores = 0;
    }

    private boolean verificarJogador(Jogador jogador){
        for(Jogador j : jogadores){
            if(j.getId() == jogador.getId()) return true;
        }

        return false;
    }

    public MensagemRetorno entrar(Jogador jogador) throws RemoteException {

        MensagemRetorno mensagem = new MensagemRetorno();
        if(this.quantidadeJogadores < 2){
            if(!verificarJogador(jogador)){
                this.jogadores.add(jogador);
                this.quantidadeJogadores++;

                mensagem.setCod(1);
                mensagem.setTexto(jogador.getNome() + ", sejam bem-vindo ao Jogo da Velha. Você é o jogador número ");

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

    public void jogar(Jogador jogador, int linha, int coluna) throws RemoteException {

    }
}
