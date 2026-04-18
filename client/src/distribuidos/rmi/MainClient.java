package distribuidos.rmi;

import java.rmi.registry.Registry;

import distribuidos.rmi.controller.FlowerController;
import distribuidos.rmi.service.config.RmiConnectionFactory;
import distribuidos.rmi.view.TerminalView;
import helper.loader.EnvLoader;

public class MainClient {

    public static void main(String[] args) {
        EnvLoader.load(".env");

        String host = EnvLoader.get("HOST").orElse("localhost");
        int port = EnvLoader.get("PORT").map(Integer::parseInt)
                .orElseThrow(() -> new RuntimeException("PORT deve ser definido no .env como um número inteiro"));

        System.out.println("Client iniciado");

        // Conecta ao servidor RMI
        Registry registry = RmiConnectionFactory.connect(host, port)
                .orElseThrow(() -> new RuntimeException("Não foi possível conectar ao servidor RMI"));

        TerminalView view = TerminalView.getInstance();
        FlowerController controller = new FlowerController(registry, view);

        do {
            controller.run();
        } while (view.readKeepRunning());

    }

}
