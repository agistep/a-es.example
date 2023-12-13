package io.agistep.utils;

import org.junit.jupiter.api.Test;

import java.util.Set;

class scanClassProviderTest {

    @Test
    void name() throws Exception {
        Set<Class<?>> strings = ScanClassProvider.scanAndGetAllClasses2("io.agistep");
        System.out.println(strings);
    }

    @Test
    void name1() {
        Set<Class<?>> classes = ScanClassProvider.scanAndGetAllClasses("io.agistep");
        System.out.println(classes);
    }
}
