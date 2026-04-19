package distribuidos.rmi.server.service;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Service responsável por lidar com a inicialização do servidor RMI e o
 * registro de stubs de serviços remotos.
 */
public class RMIService {

    private final Registry registry;

    private static RMIService instance;

    public static RMIService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RMIService não foi inicializado. Chame initalizeRMIServer primeiro.");
        }
        return instance;
    }

    private RMIService(Registry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("O RMI Registry não pode ser nulo.");
        }
        this.registry = registry;
        instance = this;
    }

    public void unbindRegistry() {
        try {
            UnicastRemoteObject.unexportObject(this.registry, true);
            System.out.println("Registro removido.");
        } catch (NoSuchObjectException ignored) {
        }
    }

    /**
     * Inicializa o servidor RMI criando um registry na porta especificada e
     * configurando o hostname.
     * 
     * @param host O hostname ou endereço IP para o qual o servidor RMI deve se
     *             vincular.
     * @param port A porta na qual o RMI Registry deve ser criado.
     * @return Uma instância de RMIService com o registry criado e pronto para uso.
     * @throws Exception Erro ao iniciar o RMI Registry ou configurar o servidor
     *                   RMI.
     */
    public static RMIService initalizeRMIServer(String host, int port) throws Exception {
        try {
            System.setProperty("java.rmi.server.hostname", host);
            Registry registry = LocateRegistry.createRegistry(port);
            RMIService rmiService = new RMIService(registry);
            System.out.println("Servidor RMI iniciado em " + host + ":" + port);
            return rmiService;
        } catch (RemoteException e) {
            System.err.println("Erro ao iniciar o RMI Registry: " + e.getMessage());
            throw new Exception("Falha ao iniciar o RMI Registry", e);
        }
    }

    /**
     * Registra um stub de serviço remoto no RMI Registry.
     * 
     * @param service     O serviço remoto a ser registrado.
     * @param serviceName O nome sob o qual o serviço será registrado.
     * @throws Exception Erro ao registrar o serviço.
     */
    public void registryStub(Remote service, String serviceName) throws Exception {
        try {
            Remote remoteObject = UnicastRemoteObject.exportObject(service, 0);
            this.registry.rebind(serviceName, remoteObject);
            System.out.println(serviceName + " registrado no RMI Registry.");
        } catch (RemoteException e) {
            System.err.println("Erro ao registrar o serviço: " + e.getMessage());
            throw new Exception("Falha ao registrar o serviço", e);
        }
    }

}
