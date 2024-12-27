package app;

import rmi_api.ResponseRMI;
import rmi_api.ServicesAPI;

import java.rmi.RemoteException;

public class Services implements ServicesAPI {

    @Override
    public ResponseRMI processUserQuery(String userQuery) throws RemoteException {
        return null;
    }

    @Override
    public ResponseRMI getAllAnnonces() throws RemoteException {
        return null;
    }



}
