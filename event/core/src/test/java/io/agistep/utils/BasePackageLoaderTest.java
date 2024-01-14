package io.agistep.utils;

import io.agistep.event.exception.GamjaComponentCreationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BasePackageLoaderTest {

    @Test
    void exceptionWhenBasePackageIsNotSet() {
        System.clearProperty("basePackage");

         assertThatThrownBy(BasePackageLoader::load)
                 .isInstanceOf(GamjaComponentCreationException.class)
                 .hasMessageContaining("BasePackage should be set as a system property.");
    }
}
