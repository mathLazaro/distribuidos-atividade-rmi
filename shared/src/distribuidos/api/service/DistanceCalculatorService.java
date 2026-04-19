package distribuidos.api.service;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import distribuidos.api.model.FlowerFeature;

/**
 * Interface remota para o serviço de cálculo de distância entre características
 * de flores. Extende a interface Remote, permitindo que seus métodos sejam
 * invocados remotamente via RMI
 */
public interface DistanceCalculatorService extends Remote {

    double calculateDistanceByEclidean(FlowerFeature features1, FlowerFeature features2) throws RemoteException;

    double calculateDistanceByCityBlock(FlowerFeature features1, FlowerFeature features2) throws RemoteException;

}