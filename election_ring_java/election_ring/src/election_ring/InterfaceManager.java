package election_ring;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface InterfaceManager extends Remote {
    public void addProcess(int id, InterfaceProcess item) throws RemoteException;
    public void removeProcess(int id) throws RemoteException;
    public void initElection(int remove) throws RemoteException;
    public void stopElection(int id, InterfaceProcess item) throws RemoteException;
    public HashMap<Integer, InterfaceProcess> executingProcesses() throws RemoteException;
}
