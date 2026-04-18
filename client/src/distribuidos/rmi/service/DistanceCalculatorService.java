package distribuidos.rmi.service;

import java.rmi.registry.Registry;

import distribuidos.rmi.model.FlowerFeature;
import distribuidos.rmi.model.constant.DistanceMethod;

public interface DistanceCalculatorService {

    double calculateDistanceByEclidean(FlowerFeature features1, FlowerFeature features2);

    double calculateDistanceByCityBlock(FlowerFeature features1, FlowerFeature features2);

    // == Client helper methods ==

    static DistanceCalculatorService fetchRemoteService(Registry registry) {
        try {
            return (DistanceCalculatorService) registry.lookup("DistanceCalculatorService");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o serviço de cálculo de distância do servidor RMI", e);
        }
    }

    default double calculateDistance(FlowerFeature first, FlowerFeature second, DistanceMethod method) {
        return switch (method) {
            case EUCLIDEAN -> calculateDistanceByEclidean(first, second);
            case CITY_BLOCK -> calculateDistanceByCityBlock(first, second);
            default -> throw new IllegalArgumentException("Método de distância desconhecido");
        };
    }

}