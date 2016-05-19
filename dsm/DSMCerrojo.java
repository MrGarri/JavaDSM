package dsm;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.*;

public class DSMCerrojo {

    Cerrojo cerrojo;
    Almacen almacen;

    Map<String, ObjetoCompartido> objetos = new HashMap<>();

    public DSMCerrojo (String nom) throws RemoteException {
        try {
            FabricaCerrojos fab_cerr = (FabricaCerrojos) Naming.lookup("//" + System.getenv("SERVIDOR") + ":" + System.getenv("PUERTO") + "/DSM_cerrojos");
            cerrojo = fab_cerr.iniciar(nom);
            almacen = (Almacen) Naming.lookup("//" + System.getenv("SERVIDOR") + ":" + System.getenv("PUERTO") + "/DSM_almacen");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void asociar(ObjetoCompartido o) {
        objetos.put(o.getCabecera().getNombre(), o);
    }
    public void desasociar(ObjetoCompartido o) {
        objetos.remove(o.getCabecera());
    }
    public boolean adquirir(boolean exc) throws RemoteException {
        cerrojo.adquirir(exc);
        return actualizarObjetos();
    }
    
    public boolean liberar() throws RemoteException {
        List<ObjetoCompartido> up = new ArrayList<>();
        for(Map.Entry<String, ObjetoCompartido> ks : objetos.entrySet()){
            ks.getValue().setVersion( ks.getValue().getCabecera().getVersion()+1 );
            up.add(ks.getValue());
        }

        almacen.escribirObjetos(up);

        return cerrojo.liberar();
   }

    private List<CabeceraObjetoCompartido> getCabeceras(){
        List<CabeceraObjetoCompartido> ret = new ArrayList<>();
        for(Map.Entry<String, ObjetoCompartido> ob : objetos.entrySet()){
            ret.add(ob.getValue().getCabecera());
        }
        return ret;
    }

    private boolean actualizarObjetos() throws RemoteException {
        boolean ret = true;
        List<ObjetoCompartido> list = almacen.leerObjetos(getCabeceras());

        if(list == null)
            return true;

        for(ObjetoCompartido oc : list){
            if(objetos.get(oc.getCabecera().getNombre()).setObjeto(oc.getObjeto())){
                objetos.get(oc.getCabecera().getNombre()).setVersion(oc.getCabecera().getVersion());
            } else {
                ret = false;
            }
        }
        return ret;
    }
}
