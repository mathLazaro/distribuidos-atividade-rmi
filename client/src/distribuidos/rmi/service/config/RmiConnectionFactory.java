package distribuidos.rmi.service.config;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

public class RmiConnectionFactory {

    public static Optional<Registry> connect(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);

            return Optional.of(registry);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
