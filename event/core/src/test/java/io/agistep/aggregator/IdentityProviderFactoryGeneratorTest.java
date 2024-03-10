package io.agistep.aggregator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityProviderFactoryGeneratorTest {

    @BeforeEach
    void setUp() {
        //설정 파일
    }

    @Test
    void generatorTest() {
        // TODO Assertions load factoryName from configuration

        ConfigIdGenerator generator = new ConfigIdGenerator();
        long gen = generator.gen();
        assertThat(gen).isEqualTo(101);
    }


}
