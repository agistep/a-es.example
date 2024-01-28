package io.agistep.todo.domain;

import io.agistep.event.EventSource;
import io.agistep.event.test.HoldingEventListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class EventApplySupport {

    @BeforeEach
    void setUp() {
        System.setProperty("basePackage", "io.agistep");
        EventSource.clearAll();
        EventSource.setListener(new HoldingEventListener());
    }

    @AfterEach
    void tearDown() {
        EventSource.setListener(null);
    }
}
