package io.agistep.event.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonSerializerTest {

    JsonSerializer sut;

    private record Foo(String name, int age) {
    }

    @Test
    void zxc() {
        sut = new JsonSerializer();
        assertThatThrownBy(() -> sut.serialize(null));
    }

    @Test
    void customType_test() throws JsonProcessingException {
        Foo foo = new Foo("name", 30);
        ObjectMapper mapper = new ObjectMapper();
        sut = new JsonSerializer();

        byte[] serialize = sut.serialize(foo);
        assertThat(serialize).isEqualTo(mapper.writeValueAsBytes(foo));
    }
}
