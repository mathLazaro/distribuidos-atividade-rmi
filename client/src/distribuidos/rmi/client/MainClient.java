package distribuidos.rmi.client;

import java.rmi.registry.Registry;

import distribuidos.rmi.client.config.RmiConnectionFactory;
import distribuidos.rmi.client.controller.FlowerController;
import distribuidos.rmi.client.view.TerminalView;
import distribuidos.util.EnvLoader;

public class MainClient {

    private static String RMI_HOST;
    private static int RMI_PORT;

    public static void main(String[] args) {
        loadEnvContext();

        System.out.printf("%nClient iniciado%n");

        // Conecta ao servidor RMI
        Registry registry = RmiConnectionFactory.connect(RMI_HOST, RMI_PORT)
                .orElseThrow(() -> new RuntimeException("Não foi possível conectar ao servidor RMI"));

        TerminalView view = TerminalView.getInstance();
        FlowerController controller = new FlowerController(registry, view);

        // Inicia o loop de interação com o usuário
        do {
            controller.run();
        } while (view.readKeepRunning());

    }

    /**
     * Carrega as variáveis de ambiente necessárias para o cliente, buscando o
     * arquivo .env na raiz do projeto client
     */
    private static void loadEnvContext() {
        EnvLoader.loadRelativeTo(MainClient.class, ".env");

        RMI_HOST = EnvLoader.get("RMI_HOST").orElse("127.0.0.1");
        RMI_PORT = EnvLoader.get("RMI_PORT").map(Integer::parseInt).orElse(1099);
    }

}
