package distribuidos.rmi.controller;

import java.rmi.registry.Registry;
import java.util.Map;

import distribuidos.rmi.model.FlowerFeature;
import distribuidos.rmi.model.Pair;
import distribuidos.rmi.model.constant.DistanceMethod;
import distribuidos.rmi.service.DistanceCalculatorService;
import distribuidos.rmi.view.TerminalView;

public class FlowerController {

    private final TerminalView view;

    private final DistanceCalculatorService calculatorService;

    public FlowerController(Registry registry, TerminalView view) {
        this.view = view;
        this.calculatorService = DistanceCalculatorService.fetchRemoteService(registry);
    }

    public void run() {
        FlowerFeature featureA = view.readFlowerFeature("A");
        FlowerFeature featureB = view.readFlowerFeature("B");
        FlowerFeature featureC = view.readFlowerFeature("C");

        DistanceMethod method = view.readDistanceMethod();

        Map<Pair<FlowerFeature, FlowerFeature>, Double> featureDistances = Map.of(
                new Pair<>(featureA, featureB), calculatorService.calculateDistance(featureA, featureB, method),
                new Pair<>(featureA, featureC), calculatorService.calculateDistance(featureA, featureC, method),
                new Pair<>(featureB, featureC), calculatorService.calculateDistance(featureB, featureC, method));

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

}
