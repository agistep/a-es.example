package io.agistep.event;

import io.agistep.aggregator.AggregateId;
import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.agistep.aggregator.IdAssignStrategy.AUTO;
import static io.agistep.aggregator.IdAssignStrategy.MANUAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdAssignorTest {

    @Test
    void exception_whenEventIsNotFirstSeq() {
        var aggregate = new Foo();
        var event = eventOfSeq(3L);
        assertThatThrownBy(() -> IdAssignor.assignIdByStrategy(aggregate, event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Event eventOfSeq(long nextSeq) {
        return EventMaker.make(
                1L,
                100L,
                nextSeq,
                FooCreated.class.getName(),
                LocalDateTime.MIN,
                new FooCreated()
        );
    }

    @Test
    void aggregateIdIsAssignedByDefaultWhenEventIsFirst() {
        var aggregate = new Foo();
        var createdEvent = eventOfSeq(0L);
        assertThat(aggregate.getId()).isEqualTo(0);

        IdAssignor.assignIdByStrategy(aggregate, createdEvent);

        assertThat(aggregate.getId()).isEqualTo(100L);
    }

    @Test
    void idAssignedWhenAutoStrategy() {
        var aggregate = new FooAuto();
        var createdEvent = eventOfSeq(0L);

        IdAssignor.assignIdByStrategy(aggregate, createdEvent);

        assertThat(aggregate.getId()).isEqualTo(100L);
    }

    @Test
    void idNotAssignedWhenManualStrategy() {
        var aggregate = new FooManual();
        var createdEvent = eventOfSeq(0L);

        IdAssignor.assignIdByStrategy(aggregate, createdEvent);

        assertThat(aggregate.getId()).isEqualTo(0);
    }
}

class FooAuto {

    @AggregateId(assignStrategy = AUTO)
    long id;

    public FooAuto() {}

    public long getId() {
        return id;
    }
}

class FooManual {

    @AggregateId(assignStrategy = MANUAL)
    long id;

    public FooManual() {}

    public long getId() {
        return id;
    }
}
