package io.agistep.event;

import io.agistep.event.serialization.JsonSerializer;
import io.agistep.event.serialization.NoOpSerializer;
import io.agistep.event.serialization.ProtocolBufferSerializer;
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

    @Test
    void jsonSerializer() {
        sut = new InitialContext();
        assertThat(sut).isNotNull();

        Serializer serializer = sut.serializerLookup("Json");
        assertThat(serializer).isInstanceOf(JsonSerializer.class);
    }

    @Test
    void protobufSerializer() {
        sut = new InitialContext();
        assertThat(sut).isNotNull();

        Serializer serializer = sut.serializerLookup("ProtocolBuffer");
        assertThat(serializer).isInstanceOf(ProtocolBufferSerializer.class);
    }
}