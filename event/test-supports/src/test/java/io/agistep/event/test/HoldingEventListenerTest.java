package io.agistep.event.test;

import io.agistep.event.Events;
import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import io.agistep.foo.FooDone;
import io.agistep.foo.FooReOpened;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventFixtureBuilder.eventsWith;
import static io.agistep.event.test.EventSourcingAssertions.assertEventSourcing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HoldingEventListenerTest {
    public static final FooCreated FIRST_PAYLOAD = new FooCreated();
    public static final FooDone SECOND_PAYLOAD = new FooDone();
    public static final FooReOpened THIRD_PAYLOAD = new FooReOpened();

    HoldingEventListener sut;

    @BeforeEach
    void setUp() {
        sut = new HoldingEventListener(); // TODO 상태를 가지고 있다..
        Events.setListener(sut);
    }

    @AfterEach
    void tearDown() {
        HoldingEventLogger.init().clear();
        Events.setListener(null); // TODO  불필요한 ... 현재 Listener 는 static 으로 유지 된다.
    }

    @Test
    @DisplayName("Get Event by index")
    void get0() {
        assertThat(sut.size()).isEqualTo(0);
        assertThatThrownBy(() -> sut.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> sut.get(1000)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void case1() {
        assertEventSourcing(Foo::new)
                .given()
                .when((aggregate) -> {
                    Events.apply(aggregate, FIRST_PAYLOAD);
                    Events.apply(aggregate, SECOND_PAYLOAD);
                })
                .expected(FIRST_PAYLOAD, SECOND_PAYLOAD);
    }

    @Test
    void case2() {
        assertEventSourcing(Foo::new)
                .given(FIRST_PAYLOAD, SECOND_PAYLOAD)
                .when((aggregate) -> {
                    Events.apply(aggregate, THIRD_PAYLOAD);
                })
                .expected(THIRD_PAYLOAD);
    }

}