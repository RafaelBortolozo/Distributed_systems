package jogovelha.server;

import jogovelha.cliente.Jogador;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoVelhaInterface extends Remote {
    public MensagemRetorno entrar(Jogador jogador) throws RemoteException;
    //public void iniciarJogo() throws RemoteException;
    //public int getQuantidadeJogadores() throws RemoteException;
    public boolean getPermissaoJogadorById(int id) throws RemoteException;
    public void alternarPermissoes() throws RemoteException;
    public void jogar(Jogador jogador, int linha, int coluna) throws RemoteException;
    public boolean verificarVitoria() throws RemoteException;
    public boolean validarJogada(int linha, int coluna) throws RemoteException;
    public String printMatriz() throws RemoteException;
}
