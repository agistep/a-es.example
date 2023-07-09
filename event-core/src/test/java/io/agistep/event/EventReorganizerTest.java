package io.agistep.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.agistep.event.Events.BEGIN_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

class EventReorganizerTest {


	@Test
	void reorganize() {

		Foo aggregate = new Foo();
		Object payload = new FooCreated();
		Event anEvent = Events.builder()
				.name(payload.getClass().getName())
				.order(BEGIN_ORDER) //TODO 이전 order 를 알아야한다.
				.aggregateIdValue(1L)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();

		assertThat(aggregate.id).isNull();

		EventReorganizer.reorganize(aggregate, anEvent);

		assertThat(aggregate.id.getValue()).isEqualTo(1L);
	}
}