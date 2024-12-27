package rmi_api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicesAPI extends Remote {

    ResponseRMI processUserQuery(String userQuery) throws RemoteException;

    ResponseRMI getAllAnnonces() throws RemoteException;

}
