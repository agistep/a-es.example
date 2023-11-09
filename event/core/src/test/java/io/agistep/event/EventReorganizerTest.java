package io.agistep.event;

import io.agistep.event.test.EventListBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.agistep.event.test.EventListBuilder.forTestWith;
import static org.assertj.core.api.Assertions.assertThat;

class EventReorganizerTest {


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


		Events.reorganize(aggregate, forTestWith(1L, created)
				.next(done).build());

		assertThat(aggregate.id).isEqualTo(1L);
		assertThat(aggregate.done).isTrue();
		assertThat(Events.getLatestVersionOf(aggregate)).isEqualTo(1);

		Events.apply(aggregate, reOpened);

		assertThat(aggregate.done).isFalse();
		assertThat(Events.getLatestVersionOf(aggregate)).isEqualTo(2);

	}
}