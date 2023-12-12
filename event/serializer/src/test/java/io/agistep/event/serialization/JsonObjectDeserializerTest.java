package io.agistep.event.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonObjectDeserializerTest {

    @Test
    void primitiveType_Deserializer() {
        assertThatThrownBy(() -> new JsonObjectDeserializer(null));
    }

    @Test
    void string_Deserializer() {
        JsonObjectDeserializer sut;

        String abc = "{\"name\":\"hello World\"}";

        sut = new JsonObjectDeserializer(Map.class);

        Map deserialize = (Map) sut.deserialize(abc.getBytes());
        assertThat(deserialize.get("name")).isEqualTo("hello World");
    }

    record Foo(String name, int age){}

    @Test
    void customType_Deserializer() throws JsonProcessingException {
        Foo foo = new Foo("foo", 30);

        byte[] bytes = new ObjectMapper().writeValueAsBytes(foo);
        JsonObjectDeserializer deserializer = new JsonObjectDeserializer(Foo.class);

        Object deserialize = deserializer.deserialize(bytes);
        assertThat(foo).isEqualTo(deserialize);
    }

}