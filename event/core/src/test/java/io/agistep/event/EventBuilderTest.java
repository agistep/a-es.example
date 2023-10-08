package io.agistep.event;

import io.agistep.identity.Identity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EventBuilderTest {

	final SomePayload payload = new SomePayload();
	final Bar aggregate = new Bar(1L);

	static class Bar {
		Identity<Long> id;

		public Bar(Long id) {
			this.id = new Identity<Long>() {
				@Override
				public Long getValue() {
					return id;
				}
			};
		}
	}

	@BeforeEach
	void setUp() {
		EventList.instance().clear();
		ThreadLocalEventVersionMap.instance().clear();
	}

	@Test
	void beginEvent() {
		Event actual = new EventBuilder()
				.name(((Object) payload).getClass().getName())
				.aggregate(aggregate)
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();

		assertThat(actual.getName()).isEqualTo(SomePayload.class.getName());
		assertThat(actual.getVersion()).isEqualTo(ThreadLocalEventVersionMap.BEGIN_VERSION);
		assertThat(actual.getAggregateIdValue()).isEqualTo(1L);
		assertThat(actual.getPayload()).isSameAs(payload);
		assertThat(actual.getOccurredAt()).isEqualToIgnoringSeconds(LocalDateTime.now());
	}


	@Test
	void occurs() {
		Event actual = new EventBuilder()
				.name(((Object) payload).getClass().getName())
				.version(-1) //TODO 이전 version 를 알아야한다.
				.aggregateIdValue(aggregate.id.getValue())
				.payload(payload)
				.occurredAt(LocalDateTime.now())
				.build();

		assertThat(actual.getName()).isEqualTo(SomePayload.class.getName());
		assertThat(actual.getVersion()).isNotEqualTo(ThreadLocalEventVersionMap.BEGIN_VERSION);
		assertThat(actual.getAggregateIdValue()).isEqualTo(aggregate.id.getValue());
		assertThat(actual.getPayload()).isSameAs(payload);
		assertThat(actual.getOccurredAt()).isEqualToIgnoringSeconds(LocalDateTime.now());
	}
}