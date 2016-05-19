package dsm;
import java.rmi.*;
import java.rmi.server.*;

class CerrojoImpl extends UnicastRemoteObject implements Cerrojo {

    private int adquirido;
    private boolean exclusivo;

    CerrojoImpl() throws RemoteException {
        super();
    }

    public synchronized void adquirir (boolean exc) throws RemoteException {
        try {
            if(adquirido > 0 && (exc || exclusivo))
                this.wait();

            adquirido++;
            exclusivo = exc;
        } catch (InterruptedException e) {}
    }

    public synchronized boolean liberar() throws RemoteException {
        if(adquirido < 1)
            return false;

        if(adquirido == 1)
            this.notifyAll();

        adquirido--;
        return true;
    }
}
