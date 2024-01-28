package io.agistep.event;

import io.agistep.event.serialization.JsonDeserializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SerializerCacheTest {

    @Test
    void findDeserializerTest() {
        SerializerCache serializerCache = new SerializerCache();
        JsonDeserializer jsonDeserializer = new JsonDeserializer(String.class);
        JsonDeserializer longDeserializer = new JsonDeserializer(Long.class);
        serializerCache.addDeserializer(jsonDeserializer);
        serializerCache.addDeserializer(longDeserializer);

        Deserializer deserializer = serializerCache.getDeserializer(JsonDeserializer.class.getName(), Long.class);
        assertThat(deserializer).isEqualTo(longDeserializer);
    }
}