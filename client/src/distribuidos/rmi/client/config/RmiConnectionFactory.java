package distribuidos.rmi.client.config;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

public class RmiConnectionFactory {

    /**
     * Tenta estabelecer uma conexão com o servidor RMI usando o host e a porta
     * fornecidos.
     * 
     * @param rmiHost O endereço IP ou hostname do servidor RMI.
     * @param rmiPort A porta na qual o servidor RMI está escutando.
     * @return Um Optional contendo o Registry se a conexão for bem-sucedida, ou um
     *         Optional vazio se ocorrer um erro.
     */
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
