package calcRmi.server;

import java.rmi.RemoteException;

public class Calculator implements CalcInterface{
    @Override
    public float add(float a, float b) throws RemoteException {
        return a + b;
    }

    @Override
    public float sub(float a, float b) throws RemoteException {
        return a - b;
    }

    @Override
    public float mult(float a, float b) throws RemoteException {
        return a * b;
    }

    @Override
    public float div(float a, float b) throws RemoteException {
        return a / b;
    }
}
