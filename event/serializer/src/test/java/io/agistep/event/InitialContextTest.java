package io.agistep.event;

import io.agistep.event.serialization.NoOpSerializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InitialContextTest {

    InitialContext sut;

    @Test
    void noOpsSerializer() {
        sut = new InitialContext();
        assertThat(sut).isNotNull();

        Serializer serializer = sut.serializerLookup("");
        assertThat(serializer).isInstanceOf(NoOpSerializer.class);
    }
}