package distribuidos.api.model;

import java.io.Serializable;

/**
 * Classe que representa as dimensões de um objeto, contendo altura e largura.
 * Esta classe é imutável e implementa Serializable para permitir a
 * transmissão de objetos através de redes ou armazenamento em arquivos.
 *
 * @param height A altura do objeto, deve ser um valor positivo.
 * @param width  A largura do objeto, deve ser um valor positivo.
 */
public record Dimension(double height, double width) implements Serializable {

    public Dimension {
        if (height <= 0) {
            throw new IllegalArgumentException("A altura deve ser um valor positivo.");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("A largura deve ser um valor positivo.");
        }
    }

    @Override
    public String toString() {
        return String.format("Altura: %.2f, Largura: %.2f", height, width);
    }

}
