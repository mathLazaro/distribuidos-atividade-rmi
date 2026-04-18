package distribuidos.api.model;

import java.io.Serializable;

/**
 * Representa as características de uma flor, incluindo uma descrição e as
 * dimensões da pétala e sépala.
 */
public record FlowerFeature(String description, Dimension petalDimension, Dimension sepalDimension)
        implements Serializable {

    public FlowerFeature {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A descrição da flor não pode ser nula ou vazia.");
        }
        if (petalDimension == null) {
            throw new IllegalArgumentException("A dimensão da pétala não pode ser nula.");
        }
        if (sepalDimension == null) {
            throw new IllegalArgumentException("A dimensão da sépala não pode ser nula.");
        }
    }
}