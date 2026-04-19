package distribuidos.rmi.client.controller;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Map;

import distribuidos.api.model.FlowerFeature;
import distribuidos.api.service.DistanceCalculatorService;
import distribuidos.rmi.client.model.DistanceMethod;
import distribuidos.rmi.client.model.Pair;
import distribuidos.rmi.client.view.TerminalView;

/**
 * Controlador para gerenciar as operações relacionadas às flores no cliente
 * RMI.
 */
public class FlowerController {

    private final TerminalView view;

    private final DistanceCalculatorService calculatorService;

    public FlowerController(Registry registry, TerminalView view) {
        this.view = view;
        this.calculatorService = fetchRemoteService(registry);
    }

    /**
     * Executa o fluxo principal de interação com o usuário, solicitando as
     * características das flores, o método de cálculo de distância e exibindo a
     * flor mais próxima.
     */
    public void run() {
        // Leitura dos valores das características das flores e do método de cálculo de distância
        FlowerFeature featureA = view.readFlowerFeature("A");
        FlowerFeature featureB = view.readFlowerFeature("B");
        FlowerFeature featureC = view.readFlowerFeature("C");

        DistanceMethod method = view.readDistanceMethod();

        // Cálculo das distâncias entre as flores usando o método selecionado
        Map<Pair<FlowerFeature, FlowerFeature>, Double> featureDistances = Map.of(
                new Pair<>(featureA, featureB), calculateDistance(featureA, featureB, method),
                new Pair<>(featureA, featureC), calculateDistance(featureA, featureC, method),
                new Pair<>(featureB, featureC), calculateDistance(featureB, featureC, method));

        // Identificação do par de flores mais próximas com base nas distâncias calculadas
        Pair<FlowerFeature, FlowerFeature> closestPair = defineClosestPair(featureDistances);

        view.displayClosestPair(closestPair, featureDistances.getOrDefault(closestPair, Double.NaN));

    }

    /**
     * Define o par de flores mais próximas com base nas distâncias calculadas.
     * @param distances Um mapa contendo os pares de flores e suas respectivas distâncias calculadas.
     * @return O par de flores mais próximas.
     */
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

    /**
     * Obtém o serviço remoto de cálculo de distância do servidor RMI usando o registry fornecido.
     * @param registry O registry do servidor RMI.
     * @return O serviço de cálculo de distância.
     */
    private DistanceCalculatorService fetchRemoteService(Registry registry) {
        try {
            System.out.println("Obtendo o serviço de cálculo de distância do servidor RMI...");
            return (DistanceCalculatorService) registry.lookup("DistanceCalculatorService");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o serviço de cálculo de distância do servidor RMI", e);
        }
    }

    /**
     * Calcula a distância entre duas flores usando o método de distância especificado.
     * @param first A primeira flor.
     * @param second A segunda flor.
     * @param method O método de distância a ser usado.
     * @return A distância entre as duas flores.
     */
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
