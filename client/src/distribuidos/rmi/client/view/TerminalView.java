package distribuidos.rmi.client.view;

import java.util.Map;
import java.util.Scanner;

import distribuidos.api.model.Dimension;
import distribuidos.api.model.FlowerFeature;
import distribuidos.rmi.client.model.DistanceMethod;
import distribuidos.rmi.client.model.Pair;

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

            System.out.printf("%n");

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
            System.out.printf("%n");
            System.out.printf("Digite as dimensões da flor '%s':%n", description);

            Dimension petalDimension = readDimension("pétala");
            Dimension sepalDimension = readDimension("sépala");

            System.out.printf("%n");
            return new FlowerFeature(description, petalDimension, sepalDimension);
        } catch (Exception e) {
            System.err.println("Erro ao ler as dimensões da flor. Tente novamente.");
            return readFlowerFeature(description);
        }
    }

    private Dimension readDimension(String name) {
        System.out.printf("Dimensão da %s:%n", name);
        double width = readDouble(" . Largura: ");
        double height = readDouble(" . Altura: ");
        return new Dimension(height, width);
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = in.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }

                return Double.parseDouble(input.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.err.println("Valor inválido. Digite um número.");
            }
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

    public void displayClosestPair(Pair<FlowerFeature, FlowerFeature> closestPair, double distance) {
        System.out.printf("O par mais próximo é: %s e %s (valor: %.4f)%n",
                closestPair.first().description(), closestPair.second().description(), distance);
        System.out.printf(" --- %n%n");
    }

}
