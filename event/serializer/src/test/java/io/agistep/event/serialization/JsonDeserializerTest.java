package io.agistep.event.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonDeserializerTest {

    @Test
    void primitiveType_Deserializer() {
        assertThatThrownBy(() -> new JsonDeserializer(null));
    }

    @Test
    void string_Deserializer() {
        JsonDeserializer sut;

        String abc = "{\"name\":\"hello World\"}";

        sut = new JsonDeserializer(Map.class);

        Map deserialize = (Map) sut.deserialize(abc.getBytes());
        assertThat(deserialize.get("name")).isEqualTo("hello World");
    }

    record Foo(String name, int age){}

    @Test
    void customType_Deserializer() throws JsonProcessingException {
        Foo foo = new Foo("foo", 30);

        byte[] bytes = new ObjectMapper().writeValueAsBytes(foo);
        JsonDeserializer deserializer = new JsonDeserializer(Foo.class);

        Object deserialize = deserializer.deserialize(bytes);
        assertThat(foo).isEqualTo(deserialize);
    }

}