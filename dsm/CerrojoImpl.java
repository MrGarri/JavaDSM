package dsm;
import java.rmi.*;
import java.rmi.server.*;

class CerrojoImpl extends UnicastRemoteObject implements Cerrojo {

    private boolean adquirido;

    CerrojoImpl() throws RemoteException {
        super();
    }

    public synchronized void adquirir (boolean exc) throws RemoteException {

        // Adquirir

        adquirido = true;

    }

    public synchronized boolean liberar() throws RemoteException {
        if(!adquirido)
            return false;

        // Liberar

        adquirido = false;
        return true;
    }
}
