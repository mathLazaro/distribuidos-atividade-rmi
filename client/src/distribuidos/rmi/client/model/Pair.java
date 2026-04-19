package distribuidos.rmi.client.model;

/**
 * Classe auxiliar para representação de par de objetos
 */
public record Pair<T, U>(T first, U second) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?> pair) {
            return this.first.equals(pair.first) && this.second.equals(pair.second);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }

}