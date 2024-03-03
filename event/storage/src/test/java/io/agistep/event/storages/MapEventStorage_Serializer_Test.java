package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import io.agistep.event.serialization.JsonSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;

import static io.agistep.event.EventMaker.*;
import static io.agistep.event.EventMaker.payload;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class MapEventStorage_Serializer_Test {

    MapEventStorage sut;

    private record JsonPayloadTest(String value) {
    }

    @Test
    void JsonSerializer_test() {
        JsonPayloadTest payload = new JsonPayloadTest("value");
        Event anEvent = EventMaker.make(eventId(1L), aggregateId(1L), seq(0L), eventName(payload.getClass().getName()), occurredAt(LocalDateTime.of(2023,12,12,0,0)), payload(payload));

        JsonSerializer serializer = Mockito.spy(new JsonSerializer());
        sut = new MapEventStorage(new HashMap<>());

        sut.save(anEvent);

        verifyNoInteractions(serializer);
    }

    @Test
    void JsonDeSerializer_test() {
        JsonPayloadTest payload = new JsonPayloadTest("value");
        Event anEvent = EventMaker.make(eventId(1L), aggregateId(1L), seq(0L), eventName(payload.getClass().getName()), occurredAt(LocalDateTime.of(2023,12,12,0,0)), payload(payload));

        sut = new MapEventStorage(new HashMap<>());

        sut.save(anEvent);

        Event event = sut.findByAggregate(anEvent.getAggregateId()).get(0);

        assertThat(anEvent.getId()).isEqualTo(event.getId());
    }

}
