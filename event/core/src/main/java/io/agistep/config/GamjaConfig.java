package io.agistep.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static io.agistep.config.GamjaConfig.GamjaConfigLoader.loadConfig;

public class GamjaConfig {
    private static final String CONFIG_FILE_PATH = "src/main/resources/gamja.yml";
    private final static Logger log = LoggerFactory.getLogger("GamjaConfig");

    private static GamjaConfigProperties properties;

    static {
        properties = loadConfig(CONFIG_FILE_PATH);
    }

    public static GamjaConfigProperties getProperties() {
        return properties;
    }

    class GamjaConfigLoader {

        static GamjaConfigProperties loadConfig(String filePath) {
            try {
                Yaml yaml = new Yaml(new Constructor(GamjaConfigProperties.class, new LoaderOptions()));
                FileInputStream inputStream = new FileInputStream(filePath);
                return yaml.load(inputStream);
            } catch (FileNotFoundException e) {
                log.error("Config file not found: {}", filePath);
                return new GamjaConfigProperties();
            }
        }
    }
}
