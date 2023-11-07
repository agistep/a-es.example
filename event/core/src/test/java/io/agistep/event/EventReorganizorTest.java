package io.agistep.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EventReorganizorTest {


	@BeforeEach
	void setUp() {
		Events.clearAll();
	}

	@Test
	void reorganize() {

		Foo aggregate = new Foo();
		Object created = new FooCreated();
		Object done = new FooDone();
		Object reOpened = new FooReOpened();

		Event anEvent1 = Events.builder()
				.id(1L)
				.version(0L)

				.aggregateId(1L)

				.name(created.getClass().getName())
				.payload(created)
				.occurredAt(LocalDateTime.now())
				.build();

		Event anEvent2 = Events.builder()
				.id(2L)
				.version(1L)

				.aggregateId(1L)

				.name(done.getClass().getName())
				.payload(done)
				.occurredAt(LocalDateTime.now())
				.build();

		Events.reorganize(aggregate, new Event[]{anEvent1, anEvent2});

		assertThat(aggregate.id).isEqualTo(1L);
		assertThat(aggregate.done).isTrue();
		assertThat(Events.getLatestVersionOf(aggregate)).isEqualTo(1);

		Events.apply(aggregate, reOpened);

		assertThat(aggregate.done).isFalse();
		assertThat(Events.getLatestVersionOf(aggregate)).isEqualTo(2);

	}
}