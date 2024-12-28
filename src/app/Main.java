package app;

import rmi_api.ServicesAPI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

//the remote object is created and registered in the RMI registry with naming.rebind()
public class Main {
	public static void main(String[] args) {
		try {

			//Front connection + set up Controller (awaiting Client Request)
			Registry registry = LocateRegistry.createRegistry(5002);

			Services services = new Services();

			ServicesAPI serviceApi = (ServicesAPI) UnicastRemoteObject.exportObject(services, 0);

			registry.rebind("ServiceAPI", serviceApi);

			System.out.println("Server running successfully");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
