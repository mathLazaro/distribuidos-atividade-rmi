package distribuidos.rmi.view;

import java.util.Map;
import java.util.Scanner;

import distribuidos.rmi.model.Dimension;
import distribuidos.rmi.model.FlowerFeature;
import distribuidos.rmi.model.Pair;
import distribuidos.rmi.model.constant.DistanceMethod;

public class TerminalView {

    private static TerminalView INSTANCE;

    private final Scanner in;

    private TerminalView() {
        this.in = new Scanner(System.in);
    }

    public boolean readKeepRunning() {
        System.out.print("Deseja calcular novamente? (s/n): ");
        String option = in.nextLine().trim().toLowerCase();
        return option.equals("s") || option.equals("sim");
    }

    public static TerminalView getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TerminalView();
        }
        return INSTANCE;
    }

    public DistanceMethod readDistanceMethod() {
        try {
            System.out.println("Escolha o método de distância (1 ou 2):");
            System.out.println("1. Distância Euclidiana");
            System.out.println("2. Distância City Block");
            System.out.print("Opção: ");
            int option = in.nextInt();
            in.nextLine();

            return switch (option) {
                case 1 -> DistanceMethod.EUCLIDEAN;
                case 2 -> DistanceMethod.CITY_BLOCK;
                default -> throw new IllegalArgumentException("Opção inválida");
            };
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage() + " Tente novamente.");
            return readDistanceMethod();
        } catch (Exception e) {
            System.err.println("Erro ao ler a opção de método de distância. Tente novamente.");
            return readDistanceMethod();
        }

    }

    public FlowerFeature readFlowerFeature(String description) {
        try {
            System.out.println("Digite as dimensões da flor:");
            System.out.print("Dimensão da pétala (altura, largura): ");
            double petalHeight = in.nextDouble();
            double petalWidth = in.nextDouble();
            Dimension petalDimension = new Dimension(petalHeight, petalWidth);

            System.out.print("Dimensão da sépala (altura, largura): ");
            double sepalHeight = in.nextDouble();
            double sepalWidth = in.nextDouble();
            Dimension sepalDimension = new Dimension(sepalHeight, sepalWidth);

            return new FlowerFeature(description, petalDimension, sepalDimension);
        } catch (Exception e) {
            System.err.println("Erro ao ler as dimensões da flor. Tente novamente.");
            return readFlowerFeature(description);
        }
    }

    public void displayDistance(DistanceMethod method,
            Map<Pair<FlowerFeature, FlowerFeature>, Double> featureDistances) {
        for (Map.Entry<Pair<FlowerFeature, FlowerFeature>, Double> entry : featureDistances.entrySet()) {
            Pair<FlowerFeature, FlowerFeature> features = entry.getKey();
            Double distance = entry.getValue();
            System.out.printf("Distância entre %s e %s usando %s: %.4f%n",
                    features.first().description(), features.second().description(), method.name(), distance);
        }
    }

    public void displayClosestPair(Pair<FlowerFeature, FlowerFeature> closestPair) {
        System.out.printf("O par mais próximo é: %s e %s%n",
                closestPair.first().description(), closestPair.second().description());
    }

}
