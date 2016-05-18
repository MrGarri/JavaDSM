package dsm;
import java.rmi.*;
import java.rmi.server.*;

class CerrojoImpl extends UnicastRemoteObject implements Cerrojo {

    private boolean adquirido;
    private boolean exclusivo;

    CerrojoImpl() throws RemoteException {
        super();
    }

    public synchronized void adquirir (boolean exc) throws RemoteException {
        try {
            if(adquirido && (exc || exclusivo))
                this.wait();

            adquirido = true;
            exclusivo = exc;
        } catch (InterruptedException e) {}
    }

    public synchronized boolean liberar() throws RemoteException {
        if(!adquirido)
            return false;

        this.notifyAll();

        adquirido = false;
        return true;
    }
}
