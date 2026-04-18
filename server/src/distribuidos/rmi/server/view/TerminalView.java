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
        System.out.println("Method called: " + methodName);
        System.out.println("Features 1: " + features1);
        System.out.println("Features 2: " + features2);
    }

    public void logResult(String methodName, double result) {
        System.out.println("Result of " + methodName + ": " + result);
    }
}
