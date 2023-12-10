package io.agistep.event.storages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agistep.event.Event;
import io.agistep.event.EventSource;
import io.agistep.event.serialization.JsonSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class MapEventStorage_Serializer_Test {

    MapEventStorage sut;
    String a = "Hello Payload";
    ObjectMapper objectMapper = new ObjectMapper();

    Event anEvent = EventSource.builder()
            .id(1L)
            .seq(0L)
            .aggregateId(1L)
            .name("TEST")
            .payload(a)
            .occurredAt(LocalDateTime.of(2023, 12, 12, 0, 0))
            .build();

    private record JsonPayloadTest(String value) {
    }

    @Test
    void JsonSerializer_test() throws JsonProcessingException {

        String abc  = objectMapper.writeValueAsString(new JsonPayloadTest("value"));
        Event anEvent = EventSource.builder()
                .id(1L)
                .seq(0L)
                .aggregateId(1L)
                .name(JsonPayloadTest.class.getName())
                .payload(abc)
                .occurredAt(LocalDateTime.of(2023, 12, 12, 0, 0))
                .build();

        JsonSerializer serializer = Mockito.spy(new JsonSerializer());
        sut = new MapEventStorage(new HashMap<>());

        sut.save(anEvent);

        verifyNoInteractions(serializer);
    }

    @Test
    void JsonDeSerializer_test() throws JsonProcessingException {
        String abc  = objectMapper.writeValueAsString(new JsonPayloadTest("value"));
        Event anEvent = EventSource.builder()
                .id(1L)
                .seq(0L)
                .aggregateId(1L)
                .name(JsonPayloadTest.class.getName())
                .payload(abc)
                .occurredAt(LocalDateTime.of(2023, 12, 12, 0, 0))
                .build();

        sut = new MapEventStorage(new HashMap<>());

        sut.save(anEvent);

        Event event = sut.findByAggregate(anEvent.getAggregateId()).get(0);

        assertThat(anEvent.getId()).isEqualTo(event.getId());
    }

}
