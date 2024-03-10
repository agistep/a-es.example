package io.agistep.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GamjaConfigTest {

    @Test
    void loadConfig() {
        var properties = GamjaConfig.GamjaConfigLoader.loadConfig("src/test/resources/gamja.yml");

        assertThat(properties.getBasePackage()).isEqualTo("io.agistep");
    }
}
