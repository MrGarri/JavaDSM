package dsm;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class AlmacenImpl extends UnicastRemoteObject implements Almacen {

    private Map<String, ObjetoCompartido> contenedor;

    public AlmacenImpl() throws RemoteException {
        contenedor = new HashMap<>();
    }
    public synchronized	List<ObjetoCompartido> leerObjetos(List<CabeceraObjetoCompartido> lcab)
      throws RemoteException {

        List<ObjetoCompartido> res = new ArrayList<>();

        for(CabeceraObjetoCompartido i : lcab) {
            ObjetoCompartido oc = contenedor.get(i.getNombre());
            if(oc != null && oc.getCabecera().getVersion() > i.getVersion()) {
                res.add(oc);
            }
        }

        return (res.isEmpty()) ? null : res;
    }

    public synchronized void escribirObjetos(List<ObjetoCompartido> loc)
     throws RemoteException  {

        for(ObjetoCompartido i : loc) {
            if(! contenedor.containsKey(i.getCabecera().getNombre())) {
                contenedor.put(i.getCabecera().getNombre(), i);
            }
        }

    }
}

