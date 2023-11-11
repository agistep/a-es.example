package io.agistep.event.test;

import io.agistep.event.Event;
import io.agistep.event.Events;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static io.agistep.event.test.EventFixtureBuilder.anEventWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestEventLoggerTest {

    TestEventLogger sut;

    @BeforeEach
    void setUp() {
        sut = new TestEventLogger(); // TODO 상태를 가지고 있다..
        Events.setListener(sut);
    }

    @AfterEach
    void tearDown() {
        Events.setListener(null); // TODO  불필요한 ... 현재 Listener 는 static 으로 유지 된다.
    }

    @Test
    @DisplayName("Get Event by index")
    void get0() {
        assertThat(sut.size()).isEqualTo(0);
        assertThatThrownBy(()->sut.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(()->sut.get(1000)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void get1() {
        Foo aggregate = new Foo();
        FooCreated created = new FooCreated();

        Events.apply(aggregate, created);

        assertThat(sut.size()).isEqualTo(1);

        Event expected = anEventWith(created);

        Event actual = sut.get(0);
        Predicate<Event> pId = (e) -> e.getId() == expected.getId();
        Predicate<Event> pAggregateId = (e) -> e.getAggregateId() == expected.getAggregateId();
        Predicate<Event> pOccurredAt = (e) -> e.getOccurredAt() == expected.getOccurredAt();

        Predicate<Event> pVersion = (e) -> e.getVersion() == expected.getVersion();
        Predicate<Event> pName = (e) -> e.getName().equals(expected.getName());
        Predicate<Event> pPayload = (e) -> e.getPayload() == expected.getPayload();

        assertThat(actual).matches(pVersion);
        assertThat(actual).matches(pName);
        assertThat(actual).matches(pPayload);

    }

}