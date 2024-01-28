package io.agistep.event.test;

import io.agistep.event.EventSource;
import org.junit.jupiter.api.BeforeEach;

public class EventApplySupport {

    @BeforeEach
    void setUp() {
        System.setProperty("basePackage", "io.agistep");
        EventSource.clearAll();
    }
}
