package distribuidos.rmi;

import java.rmi.registry.Registry;

import distribuidos.rmi.controller.FlowerController;
import distribuidos.rmi.service.config.RmiConnectionFactory;
import distribuidos.rmi.view.TerminalView;

public class MainClient {

    private static final String HOST = "localhost";
    private static final int PORT = 0;

    public static void main(String[] args) {
        System.out.println("Client iniciado");

        // Conecta ao servidor RMI
        Registry registry = RmiConnectionFactory.connect(HOST, PORT)
                .orElseThrow(() -> new RuntimeException("Não foi possível conectar ao servidor RMI"));

        TerminalView view = TerminalView.getInstance();
        FlowerController controller = new FlowerController(registry, view);

        do {
            controller.run();
        } while (view.readKeepRunning());

    }

}
