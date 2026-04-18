package distribuidos.rmi.model;

public record Dimension(double height, double width) {

    public Dimension {
        if (height <= 0) {
            throw new IllegalArgumentException("A altura deve ser um valor positivo.");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("A largura deve ser um valor positivo.");
        }
    }

}
