package data_analyse;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIBackendServer {
    public static void main(String[] args) {
        try {
            // Create and export the backend service
            BackendServiceImpl service = new BackendServiceImpl();

            // Set up the RMI registry (by default, it runs on port 1099)
            String host = "0.0.0.0";  // Use 0.0.0.0 or specific backend machine IP
            LocateRegistry.createRegistry(1099);

            // Bind the service to a name in the RMI registry
            String rmiURL = "rmi://" + host + "/BackendService";  // Use the backend machine's IP or hostname
            Naming.rebind(rmiURL, service);

            System.out.println("RMI Backend Service is ready and bound to: " + rmiURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
