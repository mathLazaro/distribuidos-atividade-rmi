package distribuidos.rmi.server;

import distribuidos.rmi.server.service.RMIService;
import distribuidos.rmi.server.view.TerminalView;
import distribuidos.util.EnvLoader;

import distribuidos.rmi.server.service.DistanceCalculatorServiceImpl;

public class MainServer {

    private static String HOST;
    private static int PORT;

    public static void main(String[] args) {
        loadEnvContext();

        System.out.printf("%nServer iniciado%n");

        RMIService rmiService;
        try {
            rmiService = RMIService.initalizeRMIServer(HOST, PORT);
            rmiService.registryStub(
                    new DistanceCalculatorServiceImpl(TerminalView.getInstance()),
                    "DistanceCalculatorService");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownServer();
        }));

    }

    /**
     * Encerra o servidor RMI e remove o registro para evitar vazamento de recursos.
     */
    private static void shutdownServer() {
        try {
            System.out.printf("%n%nEncerrando servidor RMI...%n");

            RMIService.getInstance().unbindRegistry();

            System.out.println("Servidor encerrado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega as variáveis de ambiente necessárias para o cliente, buscando o
     * arquivo .env na raiz do projeto server
     */
    private static void loadEnvContext() {
        EnvLoader.loadRelativeTo(MainServer.class, ".env");

        HOST = EnvLoader.get("HOST").orElse("127.0.0.1");
        PORT = EnvLoader.get("PORT").map(Integer::parseInt).orElse(1099);
    }

}
