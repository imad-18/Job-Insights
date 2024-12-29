package data_analyse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.Pair;
import model.Annonce;

public interface BackendService extends Remote {
    Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> FrontResponse(String req) throws RemoteException;
}

