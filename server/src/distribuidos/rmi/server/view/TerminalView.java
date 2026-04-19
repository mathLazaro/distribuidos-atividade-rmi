package distribuidos.rmi.server.view;

import distribuidos.api.model.FlowerFeature;

public class TerminalView {

    private static TerminalView instance;

    private TerminalView() {
    }

    public static TerminalView getInstance() {
        if (instance == null) {
            instance = new TerminalView();
        }
        return instance;
    }

    public void logMethodInvocation(String methodName, FlowerFeature features1, FlowerFeature features2) {
        System.out.printf("%nMétodo chamado: %s%n", methodName);
        System.out.printf("Feature 1:  %s%n", features1);
        System.out.printf("Feature 2: %s%n", features2);
    }

    public void logResult(String methodName, double result) {
        System.out.printf("%nResultado do %s: %.2f%n", methodName, result);
    }
}
