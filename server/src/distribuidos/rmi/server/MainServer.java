package distribuidos.rmi.server;

import distribuidos.rmi.server.service.RMIService;
import distribuidos.rmi.server.view.TerminalView;
import distribuidos.util.EnvLoader;
import distribuidos.rmi.server.service.DistanceCalculatorServiceImpl;

public class MainServer {

    public static void main(String[] args) {

        EnvLoader.load(".env");

        String host = EnvLoader.get("HOST").orElse("127.0.0.1");
        int port = EnvLoader.get("PORT").map(Integer::parseInt).orElse(1099);

        System.out.printf("%nServer iniciado%n");

        RMIService rmiService;
        try {
            rmiService = RMIService.initalizeRMIServer(host, port);
            rmiService.registryStub(
                    new DistanceCalculatorServiceImpl(TerminalView.getInstance()),
                    "DistanceCalculatorService");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
