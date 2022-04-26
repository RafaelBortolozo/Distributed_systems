package jogovelha;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoVelhaInterface extends Remote {
    public MensagemRetorno entrar(Jogador jogador) throws RemoteException;
    public boolean permissaoIniciarJogo() throws RemoteException;
    public boolean getPermissaoJogadorById(int id) throws RemoteException;
    public boolean verificarVelha() throws RemoteException;
    public void alternarPermissoes() throws RemoteException;
    public void jogar(Jogador jogador, int linha, int coluna) throws RemoteException;
    public boolean verificarVitoria() throws RemoteException;
    public boolean validarJogada(int linha, int coluna) throws RemoteException;
    public String printMatriz() throws RemoteException;
    public void estouPronto() throws RemoteException;
    public void sair() throws RemoteException;
    public int getQuantidadeJogadoresEmJogo() throws RemoteException;
}
