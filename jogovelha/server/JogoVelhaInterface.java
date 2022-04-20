package jogovelha.server;

import jogovelha.cliente.Jogador;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoVelhaInterface extends Remote {
    public MensagemRetorno entrar(Jogador jogador) throws RemoteException;
    public void jogar(Jogador jogador, int linha, int coluna) throws RemoteException;
}
