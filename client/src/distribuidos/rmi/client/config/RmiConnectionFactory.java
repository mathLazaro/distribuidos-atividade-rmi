package distribuidos.rmi.client.config;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

public class RmiConnectionFactory {

    public static Optional<Registry> connect(String rmiHost, int rmiPort) {
        try {
            System.out.println("Conectando ao servidor RMI em " + rmiHost + ":" + rmiPort);
            Registry registry = LocateRegistry.getRegistry(rmiHost, rmiPort);

            return Optional.of(registry);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
