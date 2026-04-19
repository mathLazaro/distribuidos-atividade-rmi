package distribuidos.rmi.client.controller;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Map;

import distribuidos.api.model.FlowerFeature;
import distribuidos.api.service.DistanceCalculatorService;
import distribuidos.rmi.client.model.DistanceMethod;
import distribuidos.rmi.client.model.Pair;
import distribuidos.rmi.client.view.TerminalView;

public class FlowerController {

    private final TerminalView view;

    private final DistanceCalculatorService calculatorService;

    public FlowerController(Registry registry, TerminalView view) {
        this.view = view;
        this.calculatorService = fetchRemoteService(registry);
    }

    public void run() {
        FlowerFeature featureA = view.readFlowerFeature("A");
        FlowerFeature featureB = view.readFlowerFeature("B");
        FlowerFeature featureC = view.readFlowerFeature("C");

        DistanceMethod method = view.readDistanceMethod();

        Map<Pair<FlowerFeature, FlowerFeature>, Double> featureDistances = Map.of(
                new Pair<>(featureA, featureB), calculateDistance(featureA, featureB, method),
                new Pair<>(featureA, featureC), calculateDistance(featureA, featureC, method),
                new Pair<>(featureB, featureC), calculateDistance(featureB, featureC, method));

        Pair<FlowerFeature, FlowerFeature> closestPair = defineClosestPair(featureDistances);

        view.displayClosestPair(closestPair);

    }

    private Pair<FlowerFeature, FlowerFeature> defineClosestPair(
            Map<Pair<FlowerFeature, FlowerFeature>, Double> distances) {

        if (distances == null || distances.size() < 1) {
            throw new IllegalArgumentException("O mapa de distâncias deve conter pelo menos um par");
        }

        Pair<FlowerFeature, FlowerFeature> closestPair = null;
        double minDistance = Double.MAX_VALUE;

        for (var entry : distances.entrySet()) {
            if (entry.getValue() < minDistance) {
                minDistance = entry.getValue();
                closestPair = entry.getKey();
            }
        }

        return closestPair;
    }

    private DistanceCalculatorService fetchRemoteService(Registry registry) {
        try {
            System.out.println("Obtendo o serviço de cálculo de distância do servidor RMI...");
            return (DistanceCalculatorService) registry.lookup("DistanceCalculatorService");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o serviço de cálculo de distância do servidor RMI", e);
        }
    }

    private double calculateDistance(FlowerFeature first, FlowerFeature second, DistanceMethod method) {
        try {
            return switch (method) {
                case EUCLIDEAN -> this.calculatorService.calculateDistanceByEclidean(first, second);
                case CITY_BLOCK -> this.calculatorService.calculateDistanceByCityBlock(first, second);
                default -> throw new IllegalArgumentException("Método de distância desconhecido");
            };
        } catch (RemoteException e) {
            throw new RuntimeException("Erro ao calcular a distância remotamente", e);
        }
    }

}
