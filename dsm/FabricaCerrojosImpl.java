package dsm;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class FabricaCerrojosImpl extends UnicastRemoteObject implements FabricaCerrojos {

    private Map<String, Cerrojo> cerrojos = new HashMap<>();

    public FabricaCerrojosImpl() throws RemoteException {
    }

    public synchronized	Cerrojo iniciar(String s) throws RemoteException {
        Cerrojo c = cerrojos.get(s);
        if (c == null) {
            c = new CerrojoImpl();
            cerrojos.put(s, c);
        }
        return c;
    }
}

