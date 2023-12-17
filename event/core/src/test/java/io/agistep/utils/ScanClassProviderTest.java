package io.agistep.utils;

import org.junit.jupiter.api.Test;

import java.util.Set;

class ScanClassProviderTest {

    @Test
    void name1() {
        Set<Class<?>> classes = ScanClassProvider.scanAllClassesIn("io.agistep");
        System.out.println(classes);
    }

    @Test
    void name() throws Exception {
        Set<Class<?>> strings = ScanClassProvider.scanAndGetAllClasses2("io.agistep");
        System.out.println(strings);
    }
}
