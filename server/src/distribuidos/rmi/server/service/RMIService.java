package distribuidos.rmi.server.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIService {

    private final Registry registry;

    private RMIService(Registry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("O RMI Registry não pode ser nulo.");
        }
        this.registry = registry;
    }

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
