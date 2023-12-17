package io.agistep.event;

import io.agistep.event.serialization.JsonSerializer;
import io.agistep.event.serialization.NoOpDeserializer;
import io.agistep.event.serialization.NoOpSerializer;
import io.agistep.event.serialization.ProtocolBufferSerializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class ServiceLocatorTest {


    @Test
    void noOpsSerializerTest() {
        Serializer sut = ServiceLocator.getSerializer("nothing");
        assertThat(sut).isInstanceOf(NoOpSerializer.class);
    }

    @Test
    void getJsonSerializerTest() {
        Serializer sut = ServiceLocator.getSerializer("Json");
        assertThat(sut).isInstanceOf(JsonSerializer.class);
    }

    @Test
    void getProtoBufferSerializerTest() {
        Serializer sut = ServiceLocator.getSerializer("ProtocolBuffer");
        assertThat(sut).isInstanceOf(ProtocolBufferSerializer.class);
    }

    @Test
    @SuppressWarnings("InstantiationOfUtilityClass")
    void cacheTest() {
        Cache cache = spy(new Cache());
        new ServiceLocator(cache);

        ServiceLocator.getSerializer("Json");
        ServiceLocator.getSerializer("Json");
        ServiceLocator.getSerializer("Json");

        verify(cache).addSerializer(new JsonSerializer());
        verify(cache, times(3)).getSerializer("Json");
    }

    @Test
    void NoOpsDeserializationTest() {
        Deserializer sut = ServiceLocator.getDeserializer("nothing");
        assertThat(sut).isInstanceOf(NoOpDeserializer.class);
    }


}