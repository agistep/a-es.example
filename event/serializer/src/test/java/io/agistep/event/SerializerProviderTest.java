package io.agistep.event;

import io.agistep.event.serialization.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class SerializerProviderTest {


    @Test
    void noOpsSerializerTest() {
        Serializer sut = SerializerProvider.getSerializer("nothing");
        assertThat(sut).isInstanceOf(NoOpSerializer.class);
    }

    @Test
    void getJsonSerializerTest() {
        Serializer sut = SerializerProvider.getSerializer("Json");
        assertThat(sut).isInstanceOf(JsonSerializer.class);
    }

    @Test
    void getProtoBufferSerializerTest() {
        Serializer sut = SerializerProvider.getSerializer("ProtocolBuffer");
        assertThat(sut).isInstanceOf(ProtocolBufferSerializer.class);
    }

    @Test
    @SuppressWarnings("InstantiationOfUtilityClass")
    void cacheTest() {
        Cache cache = spy(new Cache());
        new SerializerProvider(cache);

        SerializerProvider.getSerializer("Json");
        SerializerProvider.getSerializer("Json");
        SerializerProvider.getSerializer("Json");

        verify(cache).addSerializer(new JsonSerializer());
        verify(cache, times(3)).getSerializer("io.agistep.event.serialization.JsonSerializer");
    }

    @Test
    void NoOpsDeserializationTest() {
        Deserializer sut = SerializerProvider.getDeserializer("nothing", Void.class );
        assertThat(sut).isInstanceOf(NoOpDeserializer.class);
    }

    @Test
    void JsonDeserializationTest() {


        Class<String> clazz = String.class;
        Deserializer sut = SerializerProvider.getDeserializer("Json", clazz);

        assertThat(sut).isInstanceOf(JsonDeserializer.class);
    }

    @Test
    void customSerializer() {
        // TODO 어떻게 사용자 입장에서 좀더 편하게 사용할 수 있게 만들 수 있을까?
        String name = CustomSerializer.class.getName();
        Serializer sut = SerializerProvider.getSerializer(name);
        assertThat(sut).isInstanceOf(CustomSerializer.class);
    }

    @Test
    void customDeserializer() {
        String name = CustomDeserializer.class.getName();
        Deserializer sut = SerializerProvider.getDeserializer(name, String.class);
        assertThat(sut).isInstanceOf(CustomDeserializer.class);
    }

    static class CustomSerializer implements Serializer {

        @Override
        public boolean isSupport(Object payload) {
            return false;
        }

        @Override
        public byte[] serialize(Object payload) {
            return new byte[0];
        }
    }

    static class CustomDeserializer implements Deserializer {

        private final Class<String> a;

        CustomDeserializer(Class<String> a) {
            this.a = a;
        }

        @Override
        public boolean isSupport(Object payload) {
            return false;
        }

        @Override
        public Object deserialize(byte[] byteArray) {
            return null;
        }
    }

}