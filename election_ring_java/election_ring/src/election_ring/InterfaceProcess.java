package election_ring;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceProcess extends Remote {
    public void electionMessage(ArrayList<Integer> list, int process) throws RemoteException;
    public void coordinatorMessage(ArrayList<Integer> list, int coordinator) throws RemoteException;
    public void setSuccessor(int process) throws RemoteException;

    public void fazerCoisa() throws RemoteException;
    public boolean isCoordinator() throws RemoteException;
    public int enviarCoisa() throws RemoteException;
    public void verificacaoAtivo() throws RemoteException;
    public boolean sendVerification() throws RemoteException;

    public void iniciarTimerFazer() throws RemoteException;
    public void pararTimerFazer() throws RemoteException;
    public void iniciarTimerCoordinator() throws RemoteException;
    public void pararTimerCoordinator() throws RemoteException;

}
