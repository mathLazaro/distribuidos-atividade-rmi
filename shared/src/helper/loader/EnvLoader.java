package helper.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnvLoader {

    private static final Map<String, String> env = new HashMap<>();

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

    public static Optional<String> get(String key) {
        return Optional.ofNullable(env.get(key));
    }

}
