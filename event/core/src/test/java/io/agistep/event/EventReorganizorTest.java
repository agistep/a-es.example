package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EventReorganizorTest {


	@BeforeEach
	void setUp() {
		EventHolder.instance().clearAll();
	}

	@Test
	void reorganize() {

		Foo aggregate = new Foo(()->1L);
		Object payload = new FooCreated();
		Event anEvent = new EventBuilder()
				.name(payload.getClass().getName())
				.aggregate(aggregate)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();

		EventReorganizor.reorganize(aggregate, anEvent);

		assertThat(aggregate.id.getValue()).isEqualTo(1L);
	}
}