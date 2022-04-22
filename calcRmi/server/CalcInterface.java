package calcRmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalcInterface extends Remote {
    public float add(float a, float b) throws RemoteException;
    public float sub(float a, float b) throws RemoteException;
    public float mult(float a, float b) throws RemoteException;
    public float div(float a, float b) throws RemoteException;
}
