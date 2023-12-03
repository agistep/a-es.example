package io.agistep.event.storages;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterTest {

    @Test
    void byte_to_string() {

        byte[] a = new byte[]{1, 2, 3};

        String expected = Arrays.toString(a);
        assertThat(Converter.convert(a)).isEqualTo(expected);

        byte[] bytes = expected.getBytes();
        assertThat(Converter.convert(expected)).isEqualTo(bytes);

    }
}