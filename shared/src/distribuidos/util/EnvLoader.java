package distribuidos.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * Carrega um arquivo (tipicamente ".env") procurando-o relativo à localização
     * do código compilado do {@code anchorClass}. Isso evita problemas quando o
     * processo é iniciado com um {@code cwd} diferente (ex.: execução via VS Code).
     */
    public static void loadRelativeTo(Class<?> anchorClass, String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("path não pode ser vazio");
        }

        String overridePath = System.getProperty("env.file");
        if (overridePath == null || overridePath.isBlank()) {
            overridePath = System.getenv("ENV_FILE");
        }
        if (overridePath != null && !overridePath.isBlank()) {
            load(overridePath);
            return;
        }

        Path candidate = Paths.get(path);
        if (candidate.isAbsolute()) {
            load(candidate.toString());
            return;
        }

        if (anchorClass != null) {
            try {
                Path baseDir = codeSourceDir(anchorClass);
                String moduleDirName = inferModuleDirName(anchorClass);
                Optional<Path> resolved = resolveUpwards(baseDir, candidate, moduleDirName, 8);
                if (resolved.isPresent()) {
                    load(resolved.get().toString());
                    return;
                }
            } catch (RuntimeException ignored) {
                // fallback below
            }
        }

        try {
            Path userDir = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
            String moduleDirName = anchorClass != null ? inferModuleDirName(anchorClass) : null;
            Optional<Path> resolved = resolveUpwards(userDir, candidate, moduleDirName, 8);
            if (resolved.isPresent()) {
                load(resolved.get().toString());
                return;
            }
        } catch (RuntimeException ignored) {
            // fallback below
        }

        load(path);
    }

    private static Path codeSourceDir(Class<?> anchorClass) {
        try {
            var location = anchorClass.getProtectionDomain().getCodeSource().getLocation();
            Path p = Paths.get(location.toURI());
            return Files.isRegularFile(p) ? p.getParent() : p;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<Path> resolveUpwards(Path startDir, Path relativePath, String moduleDirName, int maxLevels) {
        if (startDir == null) {
            return Optional.empty();
        }

        Path current = startDir;
        for (int i = 0; i <= maxLevels && current != null; i++) {
            Path attempt = current.resolve(relativePath).normalize();
            if (Files.exists(attempt) && Files.isRegularFile(attempt)) {
                return Optional.of(attempt);
            }

            if (moduleDirName != null && !moduleDirName.isBlank()) {
                Path attemptInModule = current.resolve(moduleDirName).resolve(relativePath).normalize();
                if (Files.exists(attemptInModule) && Files.isRegularFile(attemptInModule)) {
                    return Optional.of(attemptInModule);
                }
            }

            current = current.getParent();
        }

        return Optional.empty();
    }

    private static String inferModuleDirName(Class<?> anchorClass) {
        String name = anchorClass.getName();
        if (name.contains(".rmi.server.")) {
            return "server";
        }
        if (name.contains(".rmi.client.")) {
            return "client";
        }
        return null;
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
