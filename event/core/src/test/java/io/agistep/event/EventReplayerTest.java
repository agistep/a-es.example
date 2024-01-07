package io.agistep.event;

import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import io.agistep.foo.FooDone;
import io.agistep.foo.FooReOpened;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.agistep.event.test.EventFixtureBuilder.eventsWith;
import static org.assertj.core.api.Assertions.assertThat;

class EventReplayerTest extends EventApplySupport {


	@BeforeEach
	void setUp() {
		EventSource.clearAll();
	}

	@Test
	void replay() {

		Foo aggregate = new Foo();

		Object created = new FooCreated();
		Object done = new FooDone();
		Object reOpened = new FooReOpened();


		EventSource.replay(aggregate, eventsWith(1L, created)
				.next(done).build());

		assertThat(aggregate.getId()).isEqualTo(1L);
		assertThat(aggregate.isDone()).isTrue();
		assertThat(EventSource.getLatestSeqOf(aggregate)).isEqualTo(1);

		EventSource.apply(aggregate, reOpened);

		assertThat(aggregate.isDone()).isFalse();
		assertThat(EventSource.getLatestSeqOf(aggregate)).isEqualTo(2);

	}
}
