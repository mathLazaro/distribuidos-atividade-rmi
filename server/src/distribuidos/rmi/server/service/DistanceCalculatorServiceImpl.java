package distribuidos.rmi.server.service;

import distribuidos.rmi.server.view.TerminalView;
import distribuidos.api.model.FlowerFeature;
import distribuidos.api.service.DistanceCalculatorService;

public class DistanceCalculatorServiceImpl implements DistanceCalculatorService {

    private final TerminalView view;

    public DistanceCalculatorServiceImpl(TerminalView terminalView) {
        this.view = terminalView;
    }

    @Override
    public double calculateDistanceByEclidean(FlowerFeature features1, FlowerFeature features2) {
        view.logMethodInvocation("Distance by Euclidean", features1, features2);

        double sum = 0.0;
        sum += Math.pow(features1.sepalDimension().height() - features2.sepalDimension().height(), 2);
        sum += Math.pow(features1.sepalDimension().width() - features2.sepalDimension().width(), 2);

        sum += Math.pow(features1.petalDimension().height() - features2.petalDimension().height(), 2);
        sum += Math.pow(features1.petalDimension().width() - features2.petalDimension().width(), 2);

        double distance = Math.sqrt(sum);

        view.logResult("Distance by Euclidean", distance);

        return distance;
    }

    @Override
    public double calculateDistanceByCityBlock(FlowerFeature features1, FlowerFeature features2) {
        view.logMethodInvocation("Distance by City Block", features1, features2);

        double sum = 0.0;
        sum += Math.abs(features1.sepalDimension().height() - features2.sepalDimension().height());
        sum += Math.abs(features1.sepalDimension().width() - features2.sepalDimension().width());
        sum += Math.abs(features1.petalDimension().height() - features2.petalDimension().height());
        sum += Math.abs(features1.petalDimension().width() - features2.petalDimension().width());

        view.logResult("Distance by City Block", sum);

        return sum;
    }

}
