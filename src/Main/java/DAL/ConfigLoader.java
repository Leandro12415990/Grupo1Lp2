package DAL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    private static final String CONFIG_FILE = "data/Config.csv";
    private static final Map<String, String> configMap = new HashMap<>();

    static {
        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar config: " + e.getMessage(), e);
        }
    }

    public static String getPath(String key) {
        return configMap.get(key);
    }
}
