package io.agistep.event.storages;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import io.agistep.event.Event;
import io.agistep.event.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ClassNamingConvention")
class MapEventStorage_Optimistic_Locking_Test {

    MapEventStorage sut;

    Event anEvent1 = EventSource.builder()
            .id(1L)
            .seq(0L)

            .aggregateId(1L)

            .name(TestPayload.class.getName())
            .payload(TestPayload.of("Hello~~~"))
            .build();

    Event anEvent2 = EventSource.builder()
            .id(1L)
            .seq(1L)

            .aggregateId(1L)

            .name(TestPayload.class.getName())
            .payload(TestPayload.of("Hello~~~"))
            .build();

    Event anEvent3 = EventSource.builder()
            .id(1L)
            .seq(1L)
            .aggregateId(1L)
            .name(TestPayload.class.getName())
            .payload(TestPayload.of("Hello~~~"))
            .build();


    @BeforeEach
    void setUp() {
        this.sut = new MapEventStorage();
    }

    @Test
    @DisplayName("Save And Find")
    void save() {
        sut.save(anEvent1);
        sut.save(anEvent2);

        assertThatThrownBy(() -> sut.save(anEvent3)).isInstanceOf(OptimisticLockedException.class);
    }

    private static class TestPayload {
        private final String value;

        public TestPayload(String value) {
            this.value = value;
        }

        static TestPayload of(String value) {
            return new TestPayload(value);
        }
    }

}