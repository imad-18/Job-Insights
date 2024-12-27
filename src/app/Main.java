package app;

import app.Services;
import rmi_api.ServicesAPI;

import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

//the remote object is created and registered in the RMI registry with naming.rebind()
public class Main {
	public static void main(String[] args) {
		try {





			//Front connection + set up Controller (awaiting Client Request)
			Registry registry = LocateRegistry.createRegistry(1110);

			Services serveur = new Services();

			ServicesAPI serviceApi = (ServicesAPI) UnicastRemoteObject.exportObject(serveur, 0);

			Naming.rebind("//localhost:1110/Test", serviceApi);

			System.out.println("Server running successfully");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
