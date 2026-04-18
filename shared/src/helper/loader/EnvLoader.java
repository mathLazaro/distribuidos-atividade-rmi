package helper.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Classe utilitária para carregar variáveis de ambiente a partir de um arquivo
 * de ambiente (.env).
 */
public class EnvLoader {

    private static final Map<String, String> env = new HashMap<>();

    /**
     * Carrega as variáveis de ambiente de um arquivo de ambiente (.env) para um
     * mapa interno. O arquivo deve conter linhas no formato "KEY=VALUE", e linhas
     * vazias ou comentários (linhas que começam com "#") serão ignorados.
     * 
     * @param path O caminho relativo ao diretório do projeto para o arquivo .env a
     *             ser carregado.
     */
    public static void load(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.lines()
                    .filter(line -> !line.trim().isEmpty() && !line.startsWith("#"))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        env.put(parts[0].trim(), parts[1].trim());
                    });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar .env", e);
        }
    }

    /**
     * Recupera o valor de uma variável de ambiente pelo nome da chave.
     * 
     * @param key O nome da variável de ambiente a ser recuperada.
     * @return Retorna um Optional contendo o valor da variável, ou um Optional
     *         vazio se a chave não existir.
     */
    public static Optional<String> get(String key) {
        return Optional.ofNullable(env.get(key));
    }

}
