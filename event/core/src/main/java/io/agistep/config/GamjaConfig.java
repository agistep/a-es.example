package io.agistep.config;

import io.agistep.event.exception.GamjaComponentCreationException;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static io.agistep.config.GamjaConfig.GamjaConfigLoader.loadConfig;

class GamjaConfig {
    private static final String CONFIG_FILE_PATH = "gamja.yml";

    private static GamjaConfigProperties properties;

    static {
        properties = loadConfig(CONFIG_FILE_PATH);
    }


    static GamjaConfigProperties getProperties() {
        return new GamjaConfigProperties();
    }

    class GamjaConfigLoader {

        static GamjaConfigProperties loadConfig(String filePath) {
            try {
                Yaml yaml = new Yaml(new Constructor(GamjaConfigProperties.class, new LoaderOptions()));
                FileInputStream inputStream = new FileInputStream(filePath);
                return yaml.load(inputStream);
            } catch (FileNotFoundException e) {
                throw new GamjaComponentCreationException("Configuration file not found.");
            }
        }
    }
}
