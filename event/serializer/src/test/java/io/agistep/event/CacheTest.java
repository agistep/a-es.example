package io.agistep.event;

import io.agistep.event.serialization.JsonDeserializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CacheTest {

    @Test
    void findDeserializerTest() {
        Cache cache = new Cache();
        JsonDeserializer jsonDeserializer = new JsonDeserializer(String.class);
        JsonDeserializer longDeserializer = new JsonDeserializer(Long.class);
        cache.addDeserializer(jsonDeserializer);
        cache.addDeserializer(longDeserializer);

        Deserializer deserializer = cache.getDeserializer(JsonDeserializer.class.getName(), Long.class);
        assertThat(deserializer).isEqualTo(longDeserializer);
    }
}