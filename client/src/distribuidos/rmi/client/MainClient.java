package distribuidos.rmi.client;

import java.rmi.registry.Registry;

import distribuidos.rmi.client.config.RmiConnectionFactory;
import distribuidos.rmi.client.controller.FlowerController;
import distribuidos.rmi.client.view.TerminalView;
import distribuidos.util.EnvLoader;

public class MainClient {

    public static void main(String[] args) {
        EnvLoader.loadRelativeTo(MainClient.class, ".env");

        String rmiHost = EnvLoader.get("RMI_HOST").orElse("127.0.0.1");
        int rmiPort = EnvLoader.get("RMI_PORT").map(Integer::parseInt).orElse(1099);

        System.out.printf("%nClient iniciado%n");

        // Conecta ao servidor RMI
        Registry registry = RmiConnectionFactory.connect(rmiHost, rmiPort)
                .orElseThrow(() -> new RuntimeException("Não foi possível conectar ao servidor RMI"));

        TerminalView view = TerminalView.getInstance();
        FlowerController controller = new FlowerController(registry, view);

        do {
            controller.run();
        } while (view.readKeepRunning());

    }

}
