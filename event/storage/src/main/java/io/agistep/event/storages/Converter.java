package io.agistep.event.storages;

import java.util.Arrays;

class Converter {

    public static String convert(byte[] serialize) {
        return Arrays.toString(serialize);
    }

    public static byte[] convert(String expected) {
        return expected.getBytes();
    }
}
